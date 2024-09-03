package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.Entity.Member;
import project.assign.dto.MemberDTO;
import project.assign.repository.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;


    @Transactional
    @Override
    public int registerMember(MemberDTO memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
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
}
