package project.assign.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import project.assign.entity.Member;
import project.assign.repository.MemberMapper;
import project.assign.security.service.TokenService;
import project.assign.security.util.TokenUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    //private final MemberCheck memberCheck;
    private final TokenService tokenService;
    //private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 인가를 필요로 하지 않는 요청
    private static final List<Pattern> EXCLUDE_PATTERNS = Collections.unmodifiableList(Arrays.asList(
            Pattern.compile("/api/login"),
            Pattern.compile("/api/members/checkEmail"),
            Pattern.compile("/api/members/checkNickName"),
            Pattern.compile("/api/members/register"),
            Pattern.compile("/api/board/allBoard"),
            Pattern.compile("/api/board/detail/\\d+")
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenUtil.extractToken(request, "AccessToken")
                    .orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다"));

            // - AccessToken의 유효성 체크
            // 2. 유효성이 정상일 경우
            if (tokenUtil.isValidToken(token)) {
                String memberEmail = tokenUtil.getMemberEmailFromToken(token);
                //Member member = memberCheck.findByEmail(memberEmail);
                 Member member =memberMapper.findMemberByEmail(memberEmail)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
                saveAuthentication(member);
            }
            // 1-1. 유효성이 비정상일 경우
            else {
                // refreshToken을 확인하고 사용자 DB의 refreshToken과 비교한다
                String refreshToken = tokenUtil.extractToken(request, "RefreshToken")
                        .orElseThrow(() -> new RuntimeException("refreshToken이 존재하지 않습니다"));

                // member의 토큰을 검증
                // 유효성 검증
                if (tokenUtil.isValidToken(refreshToken)) {
                    String memberEmail = memberMapper.findByRefreshToken(refreshToken)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원의 토큰입니다"));
                    Member member =memberMapper.findMemberByEmail(memberEmail)
                            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

                    // 사용자의 refreshToken이 정확하다면 accessToken과 refreshToken 다시 제작
                    String accessToken = tokenService.createAccessToken(memberEmail);
                    String resetRefreshToken = tokenService.createRefreshToken(memberEmail);

                    // 사용자의 refreshToken을 다시 저장하고 AccessToken과 refreshToken을 다시 전달한다
                    memberMapper.saveRefreshToken(resetRefreshToken, memberEmail);
                    tokenUtil.sendTokens(response, accessToken, resetRefreshToken, member.getNickname());
                }
            }
            filterChain.doFilter(request, response);
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
        System.out.println(servletPath);
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(servletPath).matches());
    }
}
