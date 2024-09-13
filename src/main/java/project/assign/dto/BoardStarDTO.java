package project.assign.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.BoardStar;
import project.assign.entity.BoardStarType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardStarDTO {
    private BoardStarType starType;
    private String starShortReview;
    @Min(value = 1, message = "별점 평가는 최소 1점 이상이어야 합니다")
    private int starRating;

    public BoardStar toEntity(int boardId, int sortNo) {
        return BoardStar.builder()
                .boardId(boardId)
                .starType(starType)
                .starShortReview(starShortReview)
                .starRating(starRating)
                .sortNo(sortNo)
                .build();
    }
}
