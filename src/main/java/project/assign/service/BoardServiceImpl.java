package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.dto.BoardStarDTO;
import project.assign.dto.CommentDTO;
import project.assign.entity.*;
import project.assign.repository.BoardMapper;
import project.assign.repository.BoardStarMapper;
import project.assign.repository.CommentMapper;
import project.assign.repository.MemberMapper;
import project.assign.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        // 사용자 정보는 시큐리티를 통해 확인
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));

        try {
            // 보드를 먼저 저장하고 key를 바로 반환
            // useGeneratedKeys="true" keyProperty="boardId"
            Board board = boardRequestDTO.toEntity(member.getMemberId());
            boardMapper.boardSave(board);

            int boardId = board.getBoardId();

            // 먼저 저장된 boardId를 가지고 각 boardStar 들을 저장
            List<BoardStarDTO> boardStars = boardRequestDTO.getBoardStars();
            for (int i = 0; i < boardStars.size(); i++) {
                // 불러온 값에는 boardId 와 sort_no가 존재하지 않기 때문에 만들어주어야 한다.
                BoardStar boardStar = boardStars.get(i).toEntity(boardId, i + 1);
                boardStarMapper.boardStarSave(boardStar);
            }

            return 1;
        } catch (Exception e) {
            throw new RuntimeException("저장에 문제 발생", e);
        }
    }

    @Override
    @Transactional
    public int updateBoard(BoardRequestDTO boardRequestDTO) {
        // 1. 수정하려는 게시글 존재 확인
        Board findBoard = boardMapper.findByBoardId(boardRequestDTO.getBoardId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다"));

        // 2. 사용자의 정보와 게시글에 저장된 사용자 정보 확인
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));

        if (member.getMemberId() != findBoard.getMemberId()) {
            throw new RuntimeException("사용자의 정보가 일치하지 않습니다");
        }

        try {
            // 해당 게시글을 수정하기 위한 새로운 board 객체
            // 게시글을 수정하는 방법은 변경 체크가 아닌 모든것을 새롭게 데이터를 구성하기 때문에 put method 사용
            Board board = boardRequestDTO.toEntity(member.getMemberId());

            // 변경된 데이터 보드에 먼저 update
            boardMapper.boardUpdate(board);
            // 각 boardStar 관련 데이터 다시 저장 -> 기존의 관련 boardStar를 모두 삭제하고 다시 저장
            // 데이터의 일관성이 중요한 작업이기에 삭제 후 다시 저장으로 진행한다.
            boardStarMapper.deleteBoardStarByBoardId(board.getBoardId());

            // 별들을 다시 저장
            List<BoardStarDTO> boardStars = boardRequestDTO.getBoardStars();
            for (int i = 0; i < boardStars.size(); i++) {
                BoardStar boardStar = boardStars.get(i).toEntity(board.getBoardId(), i + 1);
                boardStarMapper.boardStarSave(boardStar);
            }
        } catch (Exception e) {
            throw new RuntimeException("수정 실패", e);
        }

        return 1;
    }

    @Override
    @Transactional
    public int deleteBoard(int id) {
        memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));

        try {
            boardMapper.deleteBoardById(id);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("삭제 중 문제 발생", e);
        }
    }

    // 기본 정렬 ( 최신순 ) 게시글 가져오기
    @Override
    public List<BoardResponseDTO> getAllBoard(int pageNumber) {
        // Board 테이블 페이징 하기
        int pageOffset = (pageNumber - 1) * 5;
        List<Integer> boards = boardMapper.getBasicBoardList(pageOffset);

        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();
        for (Integer boardId : boards) {
            // 각각의 페이징 된 데이터를 가져온다
            Board board = boardMapper.findByBoardId(boardId)
                    .orElseThrow(() -> new RuntimeException(boardId + "번의 작품은 존재하지 않습니다"));

            String nickname = memberMapper.findNicknameById(board.getMemberId())
                    .orElse("미상");


            BoardResponseDTO from = BoardResponseDTO.from(board, nickname, null);
            boardResponseDTOList.add(from);
        }
        return boardResponseDTOList;
    }

    // 조건이 담긴 정렬의 서비스
    @Override
    public List<BoardResponseDTO> sortTypeBoard(String sortOrder, String sortType, int pageNum) {
        int pageOffset = (pageNum - 1) * 5;
        List<Integer> sortedBoardIdList;
        if (sortOrder.equals("asc")) {
            sortedBoardIdList = boardStarMapper.sortASCBoardIdByStarType(sortType, pageOffset);
        } else  {
            sortedBoardIdList = boardStarMapper.sortDESCBoardIdByStarType(sortType, pageOffset);
        }

        List<Board> boardList = boardMapper.getBoardListBySortTest(sortedBoardIdList);

        List<BoardResponseDTO> boardResponseDTOList = new ArrayList<>();

        for (Integer idxNum : sortedBoardIdList) {
            for (Board board : boardList) {
                if (board.getBoardId() == idxNum) {

                    String nickname = memberMapper.findNicknameById(board.getMemberId())
                            .orElse("미상");

                    boardResponseDTOList.add(BoardResponseDTO.from(board, nickname,null));
                    break;
                }
            }
        }

        return boardResponseDTOList;
    }

    // 특정 게시글 가져오기 ( 댓글 같이 가져오기 )
    @Override
    public BoardResponseDTO findByBoardId(int boardId) {
        Board board = boardMapper.findByBoardId(boardId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다"));

        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다"));

        List<Comment> commentList = commentMapper.findByBoardId(boardId);
        return BoardResponseDTO.from(board, member.getMemberNickname(), commentList);
    }
}
