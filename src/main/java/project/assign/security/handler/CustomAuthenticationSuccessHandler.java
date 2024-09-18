package project.assign.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.assign.entity.Member;
import project.assign.repository.MemberMapper;
import project.assign.security.service.TokenService;
import project.assign.security.util.TokenUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    //private final MemberService memberService;
    //private final MemberCheck memberCheck;
    private final MemberMapper memberMapper;
    private final TokenUtil tokenUtil;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String memberEmail = extractUsername(authentication);
        String accessToken = tokenService.createAccessToken(memberEmail);
        String refreshToken = tokenService.createRefreshToken(memberEmail);

        //Member member = memberService.findByEmail(memberEmail);
        // 이슈 : AccessToken이 만료되는 시점에서 더 이상 claim조차 확인이 불가능하기 때문에 refreshToken을 요청해야 한다.
        //Member member = memberCheck.findByEmail(memberEmail);
        Member member = memberMapper.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        tokenUtil.sendTokens(response, accessToken, refreshToken, member.getNickname());

        // refreshToken 을 저장하는 로직이 필요
        memberMapper.saveRefreshToken(refreshToken, memberEmail);

        log.info("로그인에 성공하였습니다. memberEmail : {}", memberEmail);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
