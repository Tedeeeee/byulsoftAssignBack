package project.assign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.entity.Member;
import project.assign.dto.MemberRequestDTO;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.exception.ErrorCode;
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        return 1;
    }

    @Override
    public int checkNickname(String nickName) {
        // 1. 중복 체크
        boolean checkNickName = memberMapper.checkNickName(nickName);

        // 2. 닉네임 글자수, 특수 문자 제한

        if (checkNickName) {
            throw new BusinessExceptionHandler(ErrorCode.CHECK_FAIL, "사용할 수 없는 닉네임입니다");
        }
        return 1;
    }

    @Override
    public int checkEmail(String email) {
        // 1. 중복 체크
        boolean checkEmail = memberMapper.checkEmail(email);

        // 2. 특수문자 및 글자 수 제한

        if (checkEmail) {
            throw new BusinessExceptionHandler(ErrorCode.CHECK_FAIL, "사용할 수 없는 이메일입니다");
        }
        return 1;
    }

    @Override
    @Transactional
    public int deleteRefreshToken() {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "사용자를 확인할 수 없습니다"));

        try {
            memberMapper.deleteRefreshToken(member.getMemberEmail());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

        return 1;
    }
}
