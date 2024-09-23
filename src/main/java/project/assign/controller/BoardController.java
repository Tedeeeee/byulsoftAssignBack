package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.assign.commonApi.CommonResponse;
import project.assign.dto.BoardListResponseDTO;
import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.dto.SearchConditionDTO;
import project.assign.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

//    // 총 페이지 확인
//    @GetMapping("/count")
//    public CommonResponse<Integer> countPage(@RequestParam(name = "searchType") String searchType,
//                                             @RequestParam(name = "searchText") String searchText) {
//        SearchConditionDTO searchConditionDTO = new SearchConditionDTO(null, null, 0, searchType, searchText);
//        int pageCount = boardService.countBoards(searchConditionDTO);
//        return new CommonResponse<>(200, pageCount);
//    }

    // 게시글 저장 ( 인가 )
    @PostMapping("")
    public CommonResponse<Integer> insertContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        boardService.saveBoard(boardRequestDTO);
        return CommonResponse.createSuccess("게시글이 등록되었습니다");
    }

    // 게시글 수정 ( 인가 )
    @PutMapping("")
    public CommonResponse<Integer> updateContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        boardService.updateBoard(boardRequestDTO);
        return CommonResponse.createSuccess("게시글이 수정되었습니다");
    }

    // 정렬된 게시글들 가져오기
    @GetMapping("")
    public CommonResponse<BoardListResponseDTO> sortBoard(@RequestParam(name = "searchType") String searchType,
                                                            @RequestParam(name = "searchText") String searchText,
                                                            @RequestParam(name = "sortOrder") String sortOrder,
                                                            @RequestParam(name = "sortType") String sortType,
                                                            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber
                                                            ) {
        SearchConditionDTO searchConditionDTO = new SearchConditionDTO(sortOrder, sortType, pageNumber, searchType, searchText);
        BoardListResponseDTO boardListResponseDTOS = boardService.sortTypeBoard(searchConditionDTO);
        return CommonResponse.success(boardListResponseDTOS, "");
    }

    // 특정 ID를 가진 게시글 가져오기
    @GetMapping("/{id}")
    public CommonResponse<BoardResponseDTO> getBoard(@PathVariable int id) {
        BoardResponseDTO board = boardService.findByBoardId(id);
        return CommonResponse.success(board, "");
    }

    // 게시글 Soft 삭제 ( 인가 )
    @DeleteMapping("/{id}")
    public CommonResponse<Integer> deleteBoard(@PathVariable int id) {
        boardService.deleteBoard(id);
        return CommonResponse.createSuccess("게시글이 삭제되었습니다");
    }
}
