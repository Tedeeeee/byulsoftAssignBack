package project.assign.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.assign.entity.Member;
import project.assign.repository.MemberMapper;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberMapper.findMemberByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다"));

        // 탈퇴한 회원을 구분지어야 할까?
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
