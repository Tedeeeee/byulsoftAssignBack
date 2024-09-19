package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // 총 페이지 확인
    @GetMapping("/count")
    public ResponseEntity<Integer> countPage(@RequestParam(name = "searchType") String searchType,
                                             @RequestParam(name = "searchText") String searchText) {
        SearchConditionDTO searchConditionDTO = new SearchConditionDTO(null, null, 0, searchType, searchText);
        int pageCount = boardService.countBoards(searchConditionDTO);
        return ResponseEntity.ok(pageCount);
    }

    // 게시글 저장 ( 인가 )
    @PostMapping("")
    public ResponseEntity<Integer> insertContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        int result = boardService.saveBoard(boardRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 게시글 수정 ( 인가 )
    @PutMapping("")
    public ResponseEntity<Integer> updateContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        int result = boardService.updateBoard(boardRequestDTO);
        return ResponseEntity.ok(result);
    }

    // 기본 게시글을 가져오는 방법
    @GetMapping("/basic")
    public ResponseEntity<List<BoardResponseDTO>> getBoards(@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                            @RequestParam(name = "searchType") String searchType,
                                                            @RequestParam(name = "searchText") String searchText) {
        SearchConditionDTO searchConditionDTO = new SearchConditionDTO(null, null, pageNumber, searchType, searchText);
        List<BoardResponseDTO> allBoard = boardService.getBasicBoard(searchConditionDTO);
        return ResponseEntity.ok(allBoard);
    }

    // 특정 조건으로 정렬
    @GetMapping("/sort")
    public ResponseEntity<List<BoardResponseDTO>> sortBoard(@RequestParam(name = "sortOrder") String sortOrder,
                                                            @RequestParam(name = "sortType") String sortType,
                                                            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                                                            @RequestParam(name = "searchType") String searchType,
                                                            @RequestParam(name = "searchText") String searchText) {
        SearchConditionDTO searchConditionDTO = new SearchConditionDTO(sortOrder, sortType, pageNumber, searchType, searchText);
        List<BoardResponseDTO> boardResponseDTOS = boardService.sortTypeBoard(searchConditionDTO);
        return ResponseEntity.ok(boardResponseDTOS);
    }

    // 특정 ID를 가진 게시글 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDTO> getBoard(@PathVariable int id) {
        BoardResponseDTO board = boardService.findByBoardId(id);
        return ResponseEntity.ok(board);
    }

    // 게시글 Soft 삭제 ( 인가 )
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Integer> deleteBoard(@PathVariable int id) {
        int result = boardService.deleteBoard(id);
        return ResponseEntity.ok(result);
    }
}
