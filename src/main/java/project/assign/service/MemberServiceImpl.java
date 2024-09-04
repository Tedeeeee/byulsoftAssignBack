package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.Entity.Member;
import project.assign.dto.MemberDTO;
import project.assign.repository.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;
    private final MemberPasswordEncoder memberPasswordEncoder;

    @Transactional
    @Override
    public int registerMember(MemberDTO memberDto) {
        // 중요한 것은 프론트에서 이메일과 닉네임의 체크를 꼭 해야 한다는 것이다.
        // 이후의 일은 무조건 체크가 끝났다는 전제하로 진행된다는 것이다.
        // 즉, 프론트에서 true, false 를 통해 이메일과 닉네임의 체크가 종료되어야만 해당 API가 실행된다고 본다.
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberPasswordEncoder.encode(memberDto.getPassword()))
                .nickName(memberDto.getNickName())
                .name(memberDto.getName())
                .phoneNumber(memberDto.getPhoneNumber())
                .build();
        try {
            memberMapper.save(member);
        } catch (Exception e) {
            throw new RuntimeException("잘못된 입력이 존재합니다", e);
        }
        return 1;
    }

    @Override
    public void checkNickName(String nickName) {
        boolean checkNickName = memberMapper.checkNickName(nickName);

        if(checkNickName) throw new RuntimeException("이미 존재하는 닉네임 입니다");
    }

    @Override
    public void checkEmail(String email) {
        boolean checkEmail = memberMapper.checkEmail(email);

        if(checkEmail) throw new RuntimeException("해당 이메일은 이미 사용중입니다.");
    }
}
