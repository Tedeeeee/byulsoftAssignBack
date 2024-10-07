package project.assign.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardStar {
    private int boardStarId;
    private int boardId;
    private BoardStarType boardStarType;
    private String boardStarShortReview;
    private int boardStarRating;
    private int sortNo;
}
