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
    @Setter
    private int totalPage;
    private List<BoardStarDTO> boardStars = new ArrayList<>();

    public static BoardResponseDTO from(Board board) {
        // boardStars를 DTO로 변경
        List<BoardStarDTO> boardStarDTO = new ArrayList<>();
        for (BoardStar star : board.getStars()) {
            boardStarDTO.add(BoardStarDTO.from(star));
        }

        return BoardResponseDTO.builder()
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
                .build();
    }
}
