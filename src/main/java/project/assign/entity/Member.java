package project.assign.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String refreshToken;
}

