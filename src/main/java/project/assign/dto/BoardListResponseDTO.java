package project.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListResponseDTO {
    private List<BoardResponseDTO> boards;
    private int totalPages;

    public static BoardListResponseDTO from(List<BoardResponseDTO> boards, int totalPages) {
        return BoardListResponseDTO.builder()
                .boards(boards)
                .totalPages(totalPages)
                .build();
    }
}
