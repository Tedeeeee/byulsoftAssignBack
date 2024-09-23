package project.assign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.dto.*;
import project.assign.entity.*;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.exception.ErrorCode;
import project.assign.repository.BoardMapper;
import project.assign.repository.BoardStarMapper;
import project.assign.repository.CommentMapper;
import project.assign.repository.MemberMapper;
import project.assign.util.SecurityUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;
    private final CommentMapper commentMapper;
    private final BoardStarMapper boardStarMapper;

//    @Override
//    public int countBoards(SearchConditionDTO searchConditionDTO) {
//        // 특수 정렬은 사실 상관없다. 기존 검색중에 갯수가 달라질 수 있는것은 기존 검색이 달라지는 것
//        // 별 순으로 검색하는 것은 전체 게시글을 검색하는 것과 갯수가 다를 수 없다.
//
//        // 갯수가 다른 것은 지역과 제목이 포함되어 있는 것을 경우 어렵다는 것
//        // 페이지의 갯수를 세는 경우
//        // 1. 기본 정렬 ( 특정 조건으로 정렬하는 경우 => 기본 정렬과 다를것 없다 )
//        // 2. 검색어가 존재하면 총 갯수가 다르다.
//        int pageCount = boardMapper.countBoards(searchConditionDTO.getSearchType(), searchConditionDTO.getSearchText());;
//
//        return (int) Math.ceil((double) pageCount / 5);
//    }

    @Override
    @Transactional
    public void saveBoard(BoardRequestDTO boardRequestDTO) {
        // 사용자 정보는 시큐리티를 통해 확인
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다"));

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

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.INSERT_FAIL, "삽입에 실패하였습니다");
        }
    }

    @Override
    @Transactional
    public void updateBoard(BoardRequestDTO boardRequestDTO) {
        // 1. 수정하려는 게시글 존재 확인
        Board findBoard = boardMapper.findByBoardId(boardRequestDTO.getBoardId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 게시글입니다"));

        // 2. 사용자의 정보와 게시글에 저장된 사용자 정보 확인
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다"));

        if (findBoard.getWriter(member.getMemberId())) {
            throw new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다");
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
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.UPDATE_FAIL, "게시글 수정에 실패하였습니다");
        }
    }

    @Override
    @Transactional
    public void deleteBoard(int id) {
        memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다"));

        try {
            boardMapper.deleteBoardById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.DELETE_FAIL, "게시글 삭제를 실패하였습니다");
        }
    }

    // 조건이 담긴 정렬의 서비스
    @Override
    public BoardListResponseDTO sortTypeBoard(SearchConditionDTO searchConditionDTO) {
        try {
            int totalPageCnt = totalPageCount(searchConditionDTO);

            // 특정 기준으로 정렬하는 메소드
            List<Integer> sortedBoardIdList = getSortedBoardIdList(searchConditionDTO);

            // 정렬된 Board 들의 ID 리스트를 가지고 데이터를 가져온후에 Map형태로 저장한다.
            List<Board> boardList = boardMapper.getBoardListBySort(sortedBoardIdList);
            // Map 형태로 저장
            Map<Integer, Board> sortBoardIdListMap = boardList.stream()
                    .collect(Collectors.toMap(Board::getBoardId, Function.identity()));

            // 정렬된 id를 기준으로 데이터 리스트화 하기
            List<BoardResponseDTO> boardResponseDTOList = sortedBoardIdList.stream().map(boardId -> {
                        String nickname = memberMapper.findNicknameById(boardId)
                                .orElse("미상");

                        return BoardResponseDTO.forList(sortBoardIdListMap.get(boardId), nickname);
                    })
                    .toList();

            return BoardListResponseDTO.from(boardResponseDTOList, totalPageCnt);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.SELECT_FAIL, "게시글 로딩에 실패하였습니다");
        }
    }

    // 특정 게시글 가져오기 ( 댓글 같이 가져오기 )
    @Override
    public BoardResponseDTO findByBoardId(int boardId) {
        Board board = boardMapper.findByBoardId(boardId)
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 게시글입니다"));

        String nickname = memberMapper.findNicknameById(board.getMemberId())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다"));

        try{
            List<Comment> commentList = commentMapper.findByBoardId(boardId);
            return BoardResponseDTO.forBoard(board, nickname, commentList);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.SELECT_FAIL, "게시글 로딩에 실패하였습니다");
        }
    }

    private List<Integer> getSortedBoardIdList(SearchConditionDTO searchConditionDTO) {
        if (searchConditionDTO.isSortOrderEmpty()) {
            return boardMapper.getBasicBoardIdList(searchConditionDTO);
        }

        return boardStarMapper.sortBoardIdByStarType(searchConditionDTO);
    }

    private int totalPageCount(SearchConditionDTO searchConditionDTO) {
        // 특수 정렬은 사실 상관없다. 기존 검색중에 갯수가 달라질 수 있는것은 기존 검색이 달라지는 것
        // 별 순으로 검색하는 것은 전체 게시글을 검색하는 것과 갯수가 다를 수 없다.

        // 갯수가 다른 것은 지역과 제목이 포함되어 있는 것을 경우 어렵다는 것
        // 페이지의 갯수를 세는 경우
        // 1. 기본 정렬 ( 특정 조건으로 정렬하는 경우 => 기본 정렬과 다를것 없다 )
        // 2. 검색어가 존재하면 총 갯수가 다르다.
        int pageCount = boardMapper.countBoards(searchConditionDTO.getSearchType(), searchConditionDTO.getSearchText());;

        return (int) Math.ceil((double) pageCount / 5);
    }
}
