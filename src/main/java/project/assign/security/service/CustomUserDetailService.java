package project.assign.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.assign.Entity.Member;
import project.assign.repository.MemberMapper;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberMapper.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다");
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
