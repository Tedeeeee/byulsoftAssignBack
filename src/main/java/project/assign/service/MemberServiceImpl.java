package project.assign.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assign.entity.Member;
import project.assign.dto.MemberRequestDTO;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.mapper.MemberMapper;
import project.assign.security.service.TokenService;
import project.assign.util.SecurityUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        memberMapper.save(member);
    }

    @Override
    public void checkNickname(String nickName) {
        if (!Pattern.matches("^[a-zA-Z가-힣]+$", nickName)) {
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "사용할 수 없는 닉네임입니다");
        }

        boolean checkNickName = memberMapper.checkNickName(nickName);

        if (checkNickName) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,409, "사용할 수 없는 닉네임입니다");
        }
    }

    @Override
    public void checkEmail(String email) {
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email)) {
            throw new BusinessExceptionHandler(HttpStatus.BAD_REQUEST, 400, "사용할 수 없는 이메일입니다");
        }

        boolean checkEmail = memberMapper.checkEmail(email);

        if (checkEmail) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,409, "사용할 수 없는 이메일입니다");
        }
    }

    @Override
    @Transactional
    public void deleteRefreshToken(HttpServletResponse response) {
        Member member = memberMapper.findMemberByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "사용자를 확인할 수 없습니다"));

        memberMapper.deleteRefreshToken(member.getMemberEmail());

        ResponseCookie accessCookie = ResponseCookie.from(TokenService.ACCESS_TOKEN_SUBJECT, null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(TokenService.REFRESH_TOKEN_SUBJECT, null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
