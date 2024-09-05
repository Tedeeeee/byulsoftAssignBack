package project.assign.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.assign.Entity.Member;
import project.assign.repository.MemberMapper;
import project.assign.security.util.TokenUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberMapper memberMapper;
    private final TokenUtil tokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String memberEmail = extractUsername(authentication);
        String accessToken = tokenUtil.createAccessToken(memberEmail);

        Member member = memberMapper.findByEmail(memberEmail);
        if(member != null) {
            // 토큰 전달하기 ( 헤더에 실어서 )
            tokenUtil.sendAccessToken(response, accessToken, member.getNickname());
        }

        log.info("로그인에 성공하였습니다. UUID : {}", memberEmail);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

}
