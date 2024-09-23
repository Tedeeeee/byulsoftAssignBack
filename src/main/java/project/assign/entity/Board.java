package project.assign.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    private Integer boardId;
    private int memberId;
    private String memberNickname;
    private String boardTitle;
    private String boardContent;
    private String boardRegion;
    private int boardView;
    private int boardLikes;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;
    private boolean boardIsDelete;
    private List<BoardStar> stars;
    private List<Comment> comments;

    public boolean getWriter(int memberId) {
        return this.memberId != memberId;
    }
}
