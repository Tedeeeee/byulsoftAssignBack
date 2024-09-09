package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.CommentDTO;
import project.assign.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Integer> saveComment(@RequestBody @Valid CommentDTO commentDTO) {
        int result = commentService.saveComment(commentDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentDTO>> getAllComments(@PathVariable int boardId) {
        List<CommentDTO> comments = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }


}
