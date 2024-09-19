package project.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class BoardResponseDTO {
    private int boardId;
    private String memberNickname;
    private String boardTitle;
    private String boardRegion;
    private String boardContent;

    private int boardView;
    private int boardLikes;
    private String boardCreatedAt;
    private String boardUpdatedAt;
    // 여기도 DTO가 되어야 한다.
    private List<BoardStarDTO> boardStars = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    public static BoardResponseDTO from(Board board, String nickname, List<Comment> comments) {
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

        return BoardResponseDTO.builder()
                .boardId(board.getBoardId())
                .memberNickname(nickname)
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
