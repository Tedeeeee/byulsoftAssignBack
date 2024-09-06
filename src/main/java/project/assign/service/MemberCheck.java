package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.assign.Entity.Member;
import project.assign.repository.MemberMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberCheck {

    private final MemberMapper memberMapper;

    public Member findByEmail(String email) {
        return memberMapper.findByEmail(email)
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
