package project.assign.service;


import project.assign.dto.BoardDTO;

import java.util.List;

public interface BoardService {

    /**
     * 게시글 저장
     */
    int saveBoard(BoardDTO boardDTO);

    /**
     * 게시판 글 조회
     */
    List<BoardDTO> getAllBoard();

    BoardDTO findByBoardId(int id);
}
