package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.dto.BoardStarDTO;
import project.assign.dto.CommentDTO;
import project.assign.entity.Board;
import project.assign.entity.BoardStar;
import project.assign.entity.BoardStarType;
import project.assign.entity.Comment;
import project.assign.repository.BoardMapper;
import project.assign.repository.BoardStarMapper;
import project.assign.repository.CommentMapper;
import project.assign.repository.MemberMapper;
import project.assign.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;
    private final BoardStarMapper boardStarMapper;

    @Override
    public int countBoards() {
        int pageCount = boardMapper.countBoards();

        return (int) Math.ceil((double) pageCount / 5);
    }

    @Override
    @Transactional
    public int saveBoard(BoardRequestDTO boardRequestDTO) {
        int memberId = memberMapper.findMemberIdByNickname(boardRequestDTO.getNickname());

        try {
            // 보드를 먼저 저장하고
            Board board = boardRequestDTO.toEntityByMemberId(memberId);
            boardMapper.boardSave(board);

            // 먼저 저장된 boardId를 가지고 각 boardStar들을 저장
            int boardId = board.getBoardId();
            List<BoardStarDTO> boardStars = boardRequestDTO.getBoardStars();

            // 불러온 값에는 boardId 와 sort_no가 존재하지 않기 때문에 만들어주어야 한다.
            for (int i = 0; i < 5; i++) {
                BoardStar entity = boardStars.get(i).toEntity(boardId, i + 1);
                boardStarMapper.boardStarSave(entity);
            }

            return 1;
        } catch (Exception e) {
            throw new RuntimeException("저장에 문제 발생", e);
        }
    }

    @Override
    @Transactional
    public int updateBoard(BoardRequestDTO boardRequestDTO) {
        // 해당 게시글의 존재 유무 판단
        Board findBoard = boardMapper.findByBoardId(boardRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다"));

        try {
            Board board = boardRequestDTO.toEntityByMemberId(findBoard.getMemberId());

            // 변경된 데이터 보드에 먼저 update
            boardMapper.boardUpdate(board);
            // 각 boardStar 관련 데이터 다시 저장
            for (BoardStarDTO boardStar : boardRequestDTO.getBoardStars()) {
                BoardStar entity = boardStar.toEntity(board.getBoardId(), board.getBoardId());
                boardStarMapper.boardStarUpdate(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException("수정 실패", e);
        }

        return 1;
    }

    @Override
    @Transactional
    public int deleteBoard(int id) {
        SecurityUtil.getCurrentMemberId();

        try {
            boardMapper.deleteBoardById(id);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("삭제 중 문제 발생", e);
        }
    }

    @Override
    public List<BoardResponseDTO> getAllBoard(int pageNumber) {
        System.out.println(SecurityUtil.getCurrentMemberId());

        // Board 테이블 페이징 하기
        int pageOffset = (pageNumber - 1) * 5;
        List<Integer> boards = boardMapper.boardPagination(pageOffset);

        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
        for (Integer boardId : boards) {
            // 각각의 페이징 된 데이터를 가져온다
            Board getBoard = boardMapper.getBoardWithBoardStar(boardId);

            String nickname = memberMapper.findNicknameById(getBoard.getMemberId())
                    .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

            List<Comment> comments = commentMapper.findByBoardId(boardId);
            List<CommentDTO> commentList = comments.stream().map(CommentDTO::from).toList();

            BoardResponseDTO from = BoardResponseDTO.from(getBoard, nickname, commentList);
            boardResponseDTOList.add(from);
        }
        return boardResponseDTOList;
    }

    @Override
    public List<BoardResponseDTO> sortTypeBoard(String typeName, String sortType, int pageNum) {
        int pageOffset = (pageNum - 1) * 5;
        List<Integer> boardIdList;
        if (sortType.equals("asc")) {
            boardIdList = boardStarMapper.sortASCBoardIdByStarType(typeName, pageOffset);
        } else if(sortType.equals("desc")) {
            boardIdList = boardStarMapper.sortDESCBoardIdByStarType(typeName, pageOffset);
        } else {
            boardIdList = boardMapper.boardPagination(pageOffset);
        }

        List<Board> test = boardMapper.getTest(boardIdList);

        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();


        for (Integer idxNum : boardIdList) {
            for (Board board : test) {
                if (board.getBoardId() == idxNum) {
                    boardResponseDTOList.add(BoardResponseDTO.from(board, null, null));
                    break;
                }
            }
        }

        /**
         * [2,1,3,5,4]
         *
         * [1(d),2(d),3(d),4(d),5(d)]
         * cpu bound 빨ㄹ
         * io bound 쿼리를 줄이고
         */

//        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
//        for (Integer boardId : boardIdList) {
//            // 각각의 페이징 된 데이터를 가져온다
//            Board getBoard = boardMapper.getBoardWithBoardStar(boardId);
//
//            String nickname = memberMapper.findNicknameById(getBoard.getMemberId())
//                    .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));
//
//            List<Comment> comments = commentMapper.findByBoardId(boardId);
//            List<CommentDTO> commentList = comments.stream().map(CommentDTO::from).toList();
//
//            BoardResponseDTO from = BoardResponseDTO.from(getBoard, nickname, commentList);
//            boardResponseDTOList.add(from);
//        }
//

        return boardResponseDTOList;
    }

    @Override
    public BoardResponseDTO findByBoardId(int id) {
        Board board = boardMapper.getBoardWithBoardStar(id);
        String nickname = memberMapper.findNicknameById(board.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

        // 댓글 가져오기
        List<Comment> comments = commentMapper.findByBoardId(board.getBoardId());
        List<CommentDTO> commentList = comments.stream().map(CommentDTO::from).toList();

        List<BoardStar> boardStars = boardStarMapper.getBoardStarByBoardId(board.getBoardId());

        return BoardResponseDTO.from(board, nickname, commentList);
        //return Board.toDTO(board, nickname, commentList);
    }


}
