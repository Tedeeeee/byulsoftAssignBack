package project.assign.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.Board;
import project.assign.entity.BoardStar;
import project.assign.entity.BoardStarType;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDTO {
    private int id;
    @NotBlank(message = "사용자 정보가 작성되지 않았습니다")
    private String nickname;

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "지역을 입력해주세요")
    private String region;

    @NotBlank(message = "총평을 입력해주세요")
    private String contents;

    private List<BoardStarDTO> boardStars;
    private List<CommentDTO> comments = new ArrayList<>();

    private int view;
    private int likes;
    private String createdAt;
    private String updatedAt;


    public Board toEntityByMemberId(int memberId) {
        return  Board.builder()
                .boardId(id)
                .memberId(memberId)
                .title(title)
                .contents(contents)
                .region(region)
                .build();
    }
}
