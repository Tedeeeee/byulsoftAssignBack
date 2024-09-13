package project.assign.dto;

import lombok.*;
import project.assign.entity.Comment;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private int id;
    @Setter
    private int memberId;
    private int boardId;
    private String content;
    @Setter
    private String nickname;
    private String createdAt;
    private String updatedAt;

    public static Comment toEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .boardId(commentDTO.getBoardId())
                .memberId(commentDTO.getMemberId())
                .content(commentDTO.getContent())
                .nickname(commentDTO.getNickname())
                .build();
    }

    public static CommentDTO from(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .nickname(comment.getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
