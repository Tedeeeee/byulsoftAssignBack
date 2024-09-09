package project.assign.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.dto.BoardDTO;
import project.assign.dto.CommentDTO;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    private int boardId;
    private int memberId;
    private String title;
    private String contents;
    private String region;
    private String difficulty;
    private int difficultyStar;
    private String horror;
    private int horrorStar;
    private String interior;
    private int interiorStar;
    private String activity;
    private int activityStar;
    private String story;
    private int storyStar;
    private int view;
    private int likes;

    public static BoardDTO toDTO(Board board, String nickname, List<CommentDTO> comments) {
        return BoardDTO.builder()
                .id(board.getBoardId())
                .nickname(nickname)
                .title(board.getTitle())
                .content(board.getContents())
                .region(board.getRegion())
                .difficulty(board.getDifficulty())
                .difficultyRating(board.getDifficultyStar())
                .horror(board.getHorror())
                .horrorRating(board.getHorrorStar())
                .interior(board.getInterior())
                .interiorRating(board.getInteriorStar())
                .activity(board.getActivity())
                .activityRating(board.getActivityStar())
                .story(board.getStory())
                .storyRating(board.getStoryStar())
                .view(board.getView())
                .likes(board.getLikes())
                .activityRating(board.getActivityStar())
                .comments(comments)
                .build();
    }
}
