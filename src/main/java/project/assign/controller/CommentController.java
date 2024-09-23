package project.assign.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assign.commonApi.CommonResponse;
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
    public CommonResponse<List<CommentDTO>> createComment(@RequestBody @Valid CommentDTO commentDTO) {
        List<CommentDTO> result = commentService.saveComment(commentDTO);
        return CommonResponse.success(result, "댓글이 저장되었습니다");
    }

    // 특정 게시글에 담긴 댓글 가져오기
    @GetMapping("/{boardId}")
    public CommonResponse<List<CommentDTO>> getCommentsByBoardId(@PathVariable int boardId) {
        List<CommentDTO> comments = commentService.findByBoardId(boardId);
        return CommonResponse.success(comments, "");
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public CommonResponse<Integer> deleteComment(@PathVariable int commentId) {
        commentService.deleteByCommentId(commentId);
        return CommonResponse.createSuccess("댓글이 삭제되었습니다");
    }

    // 댓글 수정
    @PutMapping("")
    public CommonResponse<List<CommentDTO>> updateComment(@RequestBody CommentDTO commentDTO) {
        List<CommentDTO> comments = commentService.changeCommentContent(commentDTO);
        return CommonResponse.success(comments, "댓글이 수정되었습니다");
    }
}
