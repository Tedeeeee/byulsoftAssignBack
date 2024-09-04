package project.assign.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
}

