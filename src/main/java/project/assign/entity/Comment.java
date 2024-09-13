package project.assign.entity;

import lombok.*;

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
    private String createdAt;
    private String updatedAt;
}
