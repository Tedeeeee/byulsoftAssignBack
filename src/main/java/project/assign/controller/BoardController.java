package project.assign.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.BoardRequestDTO;
import project.assign.dto.BoardResponseDTO;
import project.assign.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    @GetMapping("/countPage")
    public ResponseEntity<Integer> countPage() {
        int pageCount = boardService.countBoards();
        return ResponseEntity.ok(pageCount);
    }

    @PostMapping("/insertContents")
    public ResponseEntity<Integer> insertContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        int result = boardService.saveBoard(boardRequestDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/updateContents")
    public ResponseEntity<Integer> updateContents(@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        int result = boardService.updateBoard(boardRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/allBoard")
    public ResponseEntity<List<BoardResponseDTO>> getBoards(@RequestParam(name = "pn", defaultValue = "1") int pageNumber) {
        List<BoardResponseDTO> allBoard = boardService.getAllBoard(pageNumber);
        return ResponseEntity.ok(allBoard);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardResponseDTO> getBoard(@PathVariable int id) {
        BoardResponseDTO board = boardService.findByBoardId(id);
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteBoard(@PathVariable int id) {
        int result = boardService.deleteBoard(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<BoardResponseDTO>> sortBoard(@RequestParam(name = "sortName") String sortName,
                                                            @RequestParam(name = "sortType") String sortType,
                                                            @RequestParam(name = "pn", defaultValue = "1") int pageNumber) {
        List<BoardResponseDTO> boardResponseDTOS = boardService.sortTypeBoard(sortName, sortType, pageNumber);
        return ResponseEntity.ok(boardResponseDTOS);
    }

}
