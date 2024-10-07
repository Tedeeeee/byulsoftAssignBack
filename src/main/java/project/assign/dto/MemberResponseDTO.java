package project.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.assign.entity.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDTO {
    private int memberId;
    private String memberEmail;
    private String memberNickname;

    public static MemberResponseDTO from(Member member) {
        return MemberResponseDTO.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .build();
    }
}
