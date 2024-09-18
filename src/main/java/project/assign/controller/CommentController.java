package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.dto.CommentDTO;
import project.assign.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<List<CommentDTO>> saveComment(@RequestBody @Valid CommentDTO commentDTO) {
        System.out.println(commentDTO.getContent());
        List<CommentDTO> result = commentService.saveComment(commentDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentDTO>> getAllComments(@PathVariable int boardId) {
        List<CommentDTO> comments = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable int commentId) {
        int result = commentService.deleteByCommentId(commentId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("")
    public ResponseEntity<List<CommentDTO>> updateComment(@RequestBody CommentDTO commentDTO) {
        List<CommentDTO> comments = commentService.changeCommentContent(commentDTO);
        return ResponseEntity.ok(comments);
    }
}
