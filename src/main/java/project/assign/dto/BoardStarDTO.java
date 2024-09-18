package project.assign.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.BoardStar;
import project.assign.entity.BoardStarType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardStarDTO {
    private BoardStarType boardStarType;
    private String boardStarShortReview;
    @Min(value = 1, message = "별점 평가는 최소 1점 이상이어야 합니다")
    private int boardStarRating;

    public BoardStar toEntity(int boardId, int sortNo) {
        return BoardStar.builder()
                .boardId(boardId)
                .boardStarType(boardStarType)
                .boardStarShortReview(boardStarShortReview)
                .boardStarRating(boardStarRating)
                .sortNo(sortNo)
                .build();
    }

    public static BoardStarDTO from(BoardStar boardStar) {
        return BoardStarDTO.builder()
                .boardStarType(boardStar.getBoardStarType())
                .boardStarShortReview(boardStar.getBoardStarShortReview())
                .boardStarRating(boardStar.getBoardStarRating())
                .build();
    }
}
