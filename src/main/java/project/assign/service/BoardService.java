package project.assign.service;


import project.assign.dto.BoardListResponseDTO;
import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.dto.SearchConditionDTO;

public interface BoardService {

    /**
     * 설명 : 게시글 저장
     * @since : 2024.09.13
     * @author : T.S YUN
     */
    void saveBoard(BoardRequestDTO boardRequestDTO);

    /**
     * 설명 : 게시글 수정
     * @since : 2024.09.13
     * @author : T.S YUN
     */
    void updateBoard(BoardRequestDTO boardRequestDTO);

    /**
     * 설명 : 게시글 삭제 처리
     * @since : 2024.09.11
     * @author : T.S YUN
     */
    void deleteBoard(int boardId);


    /**
     * 설명 : 원하는 종류로 정렬
     * @since : 2024.09.12
     * @author : T.S YUN
     */
    BoardListResponseDTO sortTypeBoard(SearchConditionDTO searchConditionDTO);

    /**
     * 설명 : 특정 게시글 불러오기
     * @since : 2024.09.12
     * @author : T.S YUN
     */
    BoardResponseDTO findByBoardId(int boardId);
}
