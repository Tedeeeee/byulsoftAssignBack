package project.assign.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final MemberPasswordEncoder memberPasswordEncoder;

    @Transactional
    @Override
    public void registerMember(MemberRequestDTO memberRequestDto) {
        checkEmail(memberRequestDto.getMemberEmail());
        checkNickname(memberRequestDto.getMemberNickname());

        Member member = memberRequestDto.toEntity(memberPasswordEncoder.encode(memberRequestDto.getMemberPassword()));

        try {
            memberMapper.save(member);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.INSERT_FAIL, "회원 등록에 실패하였습니다");
        }
    }

    @Override
    public void checkNickname(String nickName) {
        // 1. 중복 체크
        boolean checkNickName = memberMapper.checkNickName(nickName);

        // 2. 닉네임 글자수, 특수 문자 제한

        if (checkNickName) {
            throw new BusinessExceptionHandler(ErrorCode.CHECK_FAIL, "사용할 수 없는 닉네임입니다");
        }
    }

    @Override
    public void checkEmail(String email) {
        // 1. 중복 체크
        boolean checkEmail = memberMapper.checkEmail(email);

        // 2. 특수문자 및 글자 수 제한

        if (checkEmail) {
            throw new BusinessExceptionHandler(ErrorCode.CHECK_FAIL, "사용할 수 없는 이메일입니다");
        }

    }

    @Override
    @Transactional
    public void deleteRefreshToken() {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(ErrorCode.NOT_FOUND, "사용자를 확인할 수 없습니다"));

        try {
            memberMapper.deleteRefreshToken(member.getMemberEmail());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(ErrorCode.DELETE_FAIL, "로그 아웃에 실패하였습니다");
        }
    }
}
