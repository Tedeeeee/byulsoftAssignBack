package project.assign.dto;

import lombok.*;
import project.assign.entity.Comment;
import project.assign.util.TimeChangerUtil;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private int commentId;
    private int memberId;
    private int boardId;
    private String commentContent;
    private String memberNickname;
    private String commentCreatedAt;
    private String commentUpdatedAt;

    public Comment toEntity() {
        return Comment.builder()
                .boardId(boardId)
                .memberId(memberId)
                .commentContent(commentContent)
                .memberNickname(memberNickname)
                .build();
    }

    public static CommentDTO from(Comment comment) {
        return CommentDTO.builder()
                .commentId(comment.getCommentId())
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .commentContent(comment.getCommentContent())
                .memberNickname(comment.getMemberNickname())
                .commentCreatedAt(TimeChangerUtil.timeChange(comment.getCommentCreatedAt()))
                .commentUpdatedAt(TimeChangerUtil.timeChange(comment.getCommentUpdatedAt()))
                .build();
    }
}
