package project.assign.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import project.assign.Entity.Member;
import project.assign.security.service.TokenService;
import project.assign.security.util.TokenUtil;
import project.assign.service.MemberCheck;
import project.assign.service.MemberService;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final MemberCheck memberCheck;
    private final TokenService tokenService;
    //private final MemberService memberService;

    // 인가를 필요로 하지 않는 요청
    private static final List<Pattern> EXCLUDE_PATTERNS = Collections.unmodifiableList(Arrays.asList(
            Pattern.compile("/api/login"),
            Pattern.compile("/api/members/checkEmail"),
            Pattern.compile("/api/members/checkNickName"),
            Pattern.compile("/api/members/register")
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenUtil.extractAccessToken(request)
                    .orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다"));

            // refreshToken을 해석하기위해 해당 토큰의 사용자 정보를 먼저 빼낸다.
            String memberEmail = tokenUtil.getMemberEmailFromToken(token);
            Member member = memberCheck.findByEmail(memberEmail);

            // AccessToken 만료 X
            if (tokenUtil.isValidToken(token)) {
                //Member member = memberService.findByEmail(memberEmail);
                saveAuthentication(member);
                filterChain.doFilter(request, response);
            }
            // AccessToken 만료
            else {
                // refreshToken 만료시간 확인
                // 1. member.getRefreshToken을 통해 토큰을 가져온다.
                String refreshToken = member.getRefreshToken();
                // 2. 가져온 토큰의 만료시간을 확인한다.
                if (tokenUtil.isValidToken(refreshToken)) {
                    // 3. 만료시간이 지나지 않았다면 새로운 토큰을 발급하고 종료
                    String accessToken = tokenService.createAccessToken(member.getEmail());
                    tokenUtil.sendAccessToken(response, accessToken, member.getNickname());
                }
                // 4. 지났다면 재로그인 진행
                else {
                    throw new RuntimeException("로그인을 다시 진행해주세요");
                }
            }
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    public void saveAuthentication(Member member) {
        String password = member.getPassword();

        UserDetails userDetailsMember = User.builder()
                .username(member.getEmail())
                .password(password)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsMember, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private JSONObject jsonResponseWrapper(Exception e) {
        String resultMsg = "";

        if (e instanceof ExpiredJwtException) {
            resultMsg = "토큰이 만료되었습니다";
        } else if (e instanceof SignatureException) {
            resultMsg = "허용된 토큰이 아닙니다";
        } else if (e instanceof JwtException) {
            resultMsg = "토큰에 오류가 발생하였습니다";
        } else {
            resultMsg = "알수없는 오류가 발생하였습니다";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        logger.error(resultMsg, e);
        return jsonObject;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(servletPath).matches());
    }
}
