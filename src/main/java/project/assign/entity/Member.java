package project.assign.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {
    private int memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberNickname;
    private String memberName;
    private String memberPhoneNumber;
    private String memberRefreshToken;
    private boolean memberIsDelete;

}

