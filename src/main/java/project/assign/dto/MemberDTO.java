package project.assign.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String email;
    private String password;
    private String nickName;
    private String name;
    private String phoneNumber;
}
