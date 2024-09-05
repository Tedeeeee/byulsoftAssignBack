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
import project.assign.Entity.Member;
import project.assign.repository.MemberMapper;
import project.assign.security.util.TokenUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final MemberMapper memberMapper;

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
            Optional<String> tokenOption = tokenUtil.extractAccessToken(request);

            String token = tokenOption.orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다."));
            if (tokenUtil.isValidToken(token)) {
                String memberEmail = tokenUtil.getMemberEmailFromToken(token);
                // memberMapper가 아닌 Service로 변경하고 사용자가 없다면 예외처리 진행
                Member member = memberMapper.findByEmail(memberEmail);
                saveAuthentication(member);
                filterChain.doFilter(request, response);
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
        System.out.println(servletPath);
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(servletPath).matches());
    }
}
