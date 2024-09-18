package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.entity.Member;
import project.assign.dto.MemberRequestDTO;
import project.assign.repository.MemberMapper;
import project.assign.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final MemberPasswordEncoder memberPasswordEncoder;
    @Transactional
    @Override
    public int registerMember(MemberRequestDTO memberRequestDto) {
        checkEmail(memberRequestDto.getMemberEmail());
        checkNickname(memberRequestDto.getMemberNickname());

        Member member = memberRequestDto.toEntity(memberPasswordEncoder.encode(memberRequestDto.getMemberPassword()));

        try {
            memberMapper.save(member);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkNickname(String nickName) {
        // 1. 중복 체크
        boolean checkNickName = memberMapper.checkNickName(nickName);

        // 2. 닉네임 글자수, 특수 문자 제한

        if (checkNickName) throw new RuntimeException("이미 존재하는 닉네임 입니다");
    }

    @Override
    public void checkEmail(String email) {
        // 1. 중복 체크
        boolean checkEmail = memberMapper.checkEmail(email);

        // 2. 특수문자 및 글자 수 제한

        if (checkEmail) throw new RuntimeException("해당 이메일은 이미 사용중입니다.");
    }

    @Override
    @Transactional
    public int deleteRefreshToken() {
        String memberEmail = SecurityUtil.getCurrentMemberEmail();

        try {
            memberMapper.deleteRefreshToken(memberEmail);
            return 1;
        } catch (Exception e) {
            throw new RuntimeException("삭제 중 문제 발생", e);
        }
    }
}
