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
        // 중복체크가 끝났지만 혹시 모를 위험상황을 대비해서 다시 제작
        checkEmail(memberDto.getEmail());
        checkNickName(memberDto.getNickname());

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberPasswordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
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
