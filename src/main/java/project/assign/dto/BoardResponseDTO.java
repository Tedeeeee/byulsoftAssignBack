package project.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.Board;
import project.assign.entity.BoardStar;
import project.assign.util.TimeChangerUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponseDTO {
    private int id;
    private String nickname;
    private String title;
    private String region;
    private String contents;

    private int view;
    private int likes;
    private String createdAt;
    private String updatedAt;
    private List<BoardStar> boardStars = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    public static BoardResponseDTO from(Board board, String nickname, List<CommentDTO> comments) {
        return BoardResponseDTO.builder()
                .id(board.getBoardId())
                .nickname(nickname)
                .title(board.getTitle())
                .contents(board.getContents())
                .region(board.getRegion())
                .view(board.getView())
                .likes(board.getLikes())
                .createdAt(TimeChangerUtil.timeChange(board.getCreatedAt()))
                .updatedAt(TimeChangerUtil.timeChange(board.getUpdatedAt()))
                .boardStars(board.getStars())
                .comments(comments)
                .build();
    }
}
