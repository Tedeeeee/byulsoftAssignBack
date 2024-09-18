package project.assign.service;


import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.entity.BoardStarType;

import java.util.List;

public interface BoardService {

    /**
     * 설명 : 페이지 수 체크
     *
     * @author : T.S YUN
     * @since : 2024.09.12
     */
    int countBoards();

    /**
     * 게시글 저장
     */
    int saveBoard(BoardRequestDTO boardRequestDTO);

    /**
     *
     * 설명 : 게시글 수정
     * @since : 2024.09.13
     * @author : T.S YUN
     */
    int updateBoard(BoardRequestDTO boardRequestDTO);

    /**
     *
     * 설명 : 게시글 삭제 처리
     * @since : 2024.09.11
     * @author : T.S YUN
     */
    int deleteBoard(int boardId);

    /**
     * 게시판 글 전체 조회
     */
    List<BoardResponseDTO> getAllBoard(int pageNum);

    /**
     * 설명 : 원하는 종류로 정렬
     * @since : 2024.09.12
     * @author : T.S YUN
     */
    List<BoardResponseDTO> sortTypeBoard(String sortOrder, String sortType, int pageNum);

    /**
     * 게시글 단일 조회
     * 댓글이 필요하다
     */
    BoardResponseDTO findByBoardId(int boardId);
}
