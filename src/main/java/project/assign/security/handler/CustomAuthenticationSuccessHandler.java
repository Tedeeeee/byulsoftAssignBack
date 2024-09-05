package project.assign.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.assign.Entity.Member;
import project.assign.security.service.TokenService;
import project.assign.security.util.TokenUtil;
import project.assign.service.MemberCheck;
import project.assign.service.MemberService;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    //private final MemberService memberService;
    private final MemberCheck memberCheck;
    private final TokenUtil tokenUtil;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String memberEmail = extractUsername(authentication);
        String accessToken = tokenService.createAccessToken(memberEmail);

        //Member member = memberService.findByEmail(memberEmail);
        Member member = memberCheck.findByEmail(memberEmail);
        tokenUtil.sendAccessToken(response, accessToken, member.getNickname());
        // refreshToken 을 저장하는 로직이 필요
        String refreshToken = tokenService.createRefreshToken(memberEmail);
        memberCheck.saveRefreshToken(refreshToken, memberEmail);

        log.info("로그인에 성공하였습니다. memberEmail : {}", memberEmail);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
