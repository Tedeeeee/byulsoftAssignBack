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

    // 댓글 저장
    @PostMapping("")
    public ResponseEntity<List<CommentDTO>> createComment(@RequestBody @Valid CommentDTO commentDTO) {
        List<CommentDTO> result = commentService.saveComment(commentDTO);
        return ResponseEntity.ok(result);
    }

    // 특정 게시글에 담긴 댓글 가져오기
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBoardId(@PathVariable int boardId) {
        List<CommentDTO> comments = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Integer> deleteComment(@PathVariable int commentId) {
        int result = commentService.deleteByCommentId(commentId);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정
    @PutMapping("")
    public ResponseEntity<List<CommentDTO>> updateComment(@RequestBody CommentDTO commentDTO) {
        List<CommentDTO> comments = commentService.changeCommentContent(commentDTO);
        return ResponseEntity.ok(comments);
    }
}
