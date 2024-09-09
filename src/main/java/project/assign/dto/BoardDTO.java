package project.assign.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.Board;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {
    private int id;

    @NotBlank(message = "사용자 정보가 작성되지 않았습니다")
    private String nickname;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "지역을 입력해주세요")
    private String region;

    @NotBlank(message = "난이도 한줄평을 입력해주세요")
    private String difficulty;

    @Min(value = 1, message = "난이도 평가는 최소 1점 이상이어야 합니다")
    private int difficultyRating;

    @NotBlank(message = "공포도 한줄평을 입력해주세요")
    private String horror;

    @Min(value = 1, message = "공포도 평가는 최소 1점 이상이어야 합니다")
    private int horrorRating;

    @NotBlank(message = "스토리 한줄평을 입력해주세요")
    private String story;

    @Min(value = 1, message = "스토리 평가는 최소 1점 이상이어야 합니다")
    private int storyRating;

    @NotBlank(message = "활동성 한줄평을 입력해주세요")
    private String activity;

    @Min(value = 1, message = "활동성 평가는 최소 1점 이상이어야 합니다")
    private int activityRating;

    @NotBlank(message = "인테리어 한줄평을 입력해주세요")
    private String interior;

    @Min(value = 1, message = "인테리어 평가는 최소 1점 이상이어야 합니다")
    private int interiorRating;

    @NotBlank(message = "총평을 입력해주세요")
    private String content;

    private int view;
    private int likes;
    private List<CommentDTO> comments = new ArrayList<>();

    public static Board toEntity(BoardDTO boardDTO, int memberId) {
        return  Board.builder()
                .memberId(memberId)
                .title(boardDTO.getTitle())
                .contents(boardDTO.getContent())
                .region(boardDTO.getRegion())
                .difficulty(boardDTO.getDifficulty())
                .difficultyStar(boardDTO.getDifficultyRating())
                .activity(boardDTO.getActivity())
                .activityStar(boardDTO.getActivityRating())
                .horror(boardDTO.getHorror())
                .horrorStar(boardDTO.getHorrorRating())
                .story(boardDTO.getStory())
                .storyStar(boardDTO.getStoryRating())
                .interior(boardDTO.getInterior())
                .interiorStar(boardDTO.getInteriorRating())
                .build();
    }
}
