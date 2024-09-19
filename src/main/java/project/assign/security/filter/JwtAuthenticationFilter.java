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
import project.assign.dto.MemberResponseDTO;
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
            Pattern.compile("/api/members/emails/check"),
            Pattern.compile("/api/members/nicknames/check"),
            Pattern.compile("/api/members/register"),
            Pattern.compile("/api/boards/count"),
            Pattern.compile("/api/boards/basic"),
            Pattern.compile("/api/boards/region"),
            Pattern.compile("/api/boards/sort"),
            Pattern.compile("/api/boards/\\d+")
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessTokenOptional = tokenUtil.extractToken(request, "AccessToken");

            if(accessTokenOptional.isPresent()) {
                handleAccessToken(accessTokenOptional.get());
            } else {
                handleRefreshToken(request, response);
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
        String password = member.getMemberPassword();

        UserDetails userDetailsMember = User.builder()
                .username(member.getMemberEmail())
                .password(password)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsMember, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private void handleAccessToken(String accessToken) {
        if(tokenUtil.isValidToken(accessToken)) {
            String memberEmail = tokenUtil.getMemberEmailFromToken(accessToken);
            Member member = memberMapper.findMemberByEmail(memberEmail)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

            saveAuthentication(member);
        }
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = tokenUtil.extractToken(request, "RefreshToken")
                .orElseThrow(() -> new RuntimeException("refreshToken이 존재하지 않습니다"));

        String memberEmail = memberMapper.findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원의 토큰입니다"));
        Member member =memberMapper.findMemberByEmail(memberEmail)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        // 사용자의 refreshToken이 정확하다면 accessToken과 refreshToken 다시 제작
        String accessToken = tokenService.createAccessToken(memberEmail);
        String resetRefreshToken = tokenService.createRefreshToken(memberEmail);

        // 사용자의 refreshToken을 다시 저장하고 AccessToken과 refreshToken을 다시 전달한다
        memberMapper.saveRefreshToken(resetRefreshToken, memberEmail);

        MemberResponseDTO memberResponseDTO = new MemberResponseDTO();
        MemberResponseDTO memberResponse = memberResponseDTO.from(member);
        tokenUtil.sendTokens(response, accessToken, resetRefreshToken, memberResponseDTO);
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
