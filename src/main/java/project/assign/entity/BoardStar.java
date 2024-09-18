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
    private BoardStarType starType;
    private String starShortReview;
    private int starRating;
    private int sortNo;
}
