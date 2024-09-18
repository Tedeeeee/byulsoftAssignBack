package project.assign.dto;

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
public class BoardRequestDTO {
    private int boardId;
    private int memberId;

    @NotBlank(message = "제목을 입력해주세요")
    private String boardTitle;

    @NotBlank(message = "지역을 입력해주세요")
    private String boardRegion;

    @NotBlank(message = "총평을 입력해주세요")
    private String boardContent;

    private List<BoardStarDTO> boardStars = new ArrayList<>();
    private List<CommentDTO> comments = new ArrayList<>();

    private int boardView;
    private int boardLikes;
    private String boardCreatedAt;
    private String boardUpdatedAt;

    public Board toEntity(int memberId) {
        return  Board.builder()
                .boardId(boardId)
                .memberId(memberId)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardRegion(boardRegion)
                .build();
    }
}
