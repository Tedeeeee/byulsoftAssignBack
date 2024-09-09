package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.BoardDTO;
import project.assign.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/insertContents")
    public ResponseEntity<Integer> insertContents(@RequestBody @Valid BoardDTO boardDTO) {
        int result = boardService.saveBoard(boardDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/allBoard")
    public ResponseEntity<List<BoardDTO>> getBoards() {
        List<BoardDTO> allBoard = boardService.getAllBoard();
        return ResponseEntity.ok(allBoard);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable int id) {
        BoardDTO board = boardService.findByBoardId(id);
        return ResponseEntity.ok(board);
    }
}
