package project.assign.service;

import jakarta.servlet.http.Cookie;
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
import project.assign.repository.MemberMapper;
import project.assign.security.service.TokenService;
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
            throw new BusinessExceptionHandler(HttpStatus.INTERNAL_SERVER_ERROR, 500, "회원 등록에 실패하였습니다");
        }
    }

    @Override
    public void checkNickname(String nickName) {
        boolean checkNickName = memberMapper.checkNickName(nickName);

        if (checkNickName) {
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT,409, "사용할 수 없는 닉네임입니다");
        }
    }

    @Override
    public void checkEmail(String email) {
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

        try {
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

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessExceptionHandler(HttpStatus.CONFLICT, 409, "로그 아웃에 실패하였습니다");
        }
    }
}
