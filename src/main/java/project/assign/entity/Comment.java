package project.assign.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.dto.CommentDTO;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    private int id;
    private int memberId;
    private int boardId;
    private String content;
    private String nickname;
    private String createAt;
    private String updateAt;

    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .memberId(comment.getBoardId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .nickname(comment.getNickname())
                .updateAt(comment.getUpdateAt())
                .build();
    }
}
