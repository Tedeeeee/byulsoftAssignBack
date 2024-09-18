package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.assign.entity.Member;
import project.assign.repository.MemberMapper;

@Component
@RequiredArgsConstructor
public class MemberCheck {

    private final MemberMapper memberMapper;

    public Member findByEmail(String email) {
        return memberMapper.findMemberByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
    }

    public void saveRefreshToken(String refreshToken, String email) {
        memberMapper.saveRefreshToken(refreshToken, email);
    }

    public String findByRefreshToken(String refreshToken) {
        return memberMapper.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
    }
}
