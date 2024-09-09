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
    private String nickname;
    private String updateAt;

    public static Comment toEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .boardId(commentDTO.getBoardId())
                .memberId(commentDTO.getMemberId())
                .content(commentDTO.getContent())
                .nickname(commentDTO.getNickname())
                .build();
    }
}
