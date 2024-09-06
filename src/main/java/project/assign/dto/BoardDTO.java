package project.assign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    @NotBlank
    @Size(min = 1, message = "제목을 입력해주세요")
    private String title;

    @NotBlank
    @Size(min = 2, max = 5, message = "지역을 입력해주세요")
    private String region;

    @NotBlank
    @Size(min = 1, message = "총평을 입력해주세요")
    private String content;

}
