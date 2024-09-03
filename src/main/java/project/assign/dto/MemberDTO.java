package project.assign.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String password;

    @NotNull(message = "닉네임을 작성해주세요")
    private String nickName;

    @NotNull(message = "이름을 작성해주세요")
    private String name;

    @NotNull(message = "핸드폰 번호를 입력해주세요")
    private String phoneNumber;
}
