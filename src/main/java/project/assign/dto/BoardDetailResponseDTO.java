package project.assign.dto;

import lombok.*;
import project.assign.entity.Board;
import project.assign.entity.BoardStar;
import project.assign.entity.Comment;
import project.assign.util.TimeChangerUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDetailResponseDTO {
    private int boardId;
    private String memberNickname;
    private String boardTitle;
    private String boardRegion;
    private String boardContent;

    private int boardView;
    private int boardLikes;
    private String boardCreatedAt;
    private String boardUpdatedAt;
    @Setter
    private int totalPage;
    private List<BoardStarDTO> boardStars = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    public static BoardDetailResponseDTO from(Board board, List<Comment> comments) {
        // boardStars를 DTO로 변경
        List<BoardStarDTO> boardStarDTO = new ArrayList<>();
        List<CommentDTO> commentDTO = new ArrayList<>();
        for (BoardStar star : board.getStars()) {
            boardStarDTO.add(BoardStarDTO.from(star));
        }
        if (comments != null) {
            for (Comment comment : comments) {
                commentDTO.add(CommentDTO.from(comment));
            }
        }

        return BoardDetailResponseDTO.builder()
                .boardId(board.getBoardId())
                .memberNickname(board.getMemberNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardRegion(board.getBoardRegion())
                .boardView(board.getBoardView())
                .boardLikes(board.getBoardLikes())
                .boardCreatedAt(TimeChangerUtil.timeChange(board.getBoardCreatedAt()))
                .boardUpdatedAt(TimeChangerUtil.timeChange(board.getBoardUpdatedAt()))
                .boardStars(boardStarDTO)
                .comments(commentDTO)
                .build();
    }
}
