package project.assign.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.assign.entity.Member;

@Getter
@AllArgsConstructor
public class MemberRequestDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다")
    private String memberEmail;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{6,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 6글자 이상이어야 합니다")
    private String memberPassword;

    @NotBlank(message = "닉네임을 작성해주세요")
    @Size(max = 10, message = "닉네임은 10글자 이하로 작성해주세요")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "닉네임은 한글과 영문만 입력 가능합니다")
    private String memberNickname;

    @NotBlank(message = "이름을 작성해주세요")
    @Size(max = 18, message = "이름은 18글자 이하로 작성해주세요")
    @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 한글과 영문만 입력 가능합니다")
    private String memberName;

    @NotBlank(message = "핸드폰 번호를 입력해주세요")
    @Size(min = 10, max = 11, message = "핸드폰 번호는 10자리에서 11자리 사이로 입력해주세요")
    private String memberPhoneNumber;

    public Member toEntity(String memberPassword) {
        return Member.builder()
                .memberEmail(memberEmail)
                .memberPassword(memberPassword)
                .memberNickname(memberNickname)
                .memberName(memberName)
                .memberPhoneNumber(memberPhoneNumber)
                .build();
    }
}
