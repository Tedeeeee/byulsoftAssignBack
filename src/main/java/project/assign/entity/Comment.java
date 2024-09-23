package project.assign.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    private int commentId;
    private int memberId;
    private int boardId;
    private String memberNickname;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private boolean commentIsDelete;

    public boolean getWriter(int memberId) {
        return this.memberId != memberId;
    }
}
