package project.assign.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import project.assign.dto.MemberResponseDTO;
import project.assign.entity.Member;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.mapper.MemberMapper;
import project.assign.security.service.TokenService;
import project.assign.security.util.TokenUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final TokenService tokenService;
    private final MemberMapper memberMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessTokenOptional = tokenUtil.extractToken(request, "AccessToken");

            if(accessTokenOptional.isPresent()) {
                handleAccessToken(accessTokenOptional.get(), response);
            } else {
                handleRefreshToken(request, response);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("test ", e);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();

            // filterChain 종료
            return;
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

    private void handleAccessToken(String accessToken, HttpServletResponse response) throws IOException {
        if(tokenUtil.isValidToken(accessToken)) {
            String memberEmail = tokenUtil.getMemberEmailFromToken(accessToken);
            Member member = memberMapper.findMemberByEmail(memberEmail)
                    .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "사용자를 확인할 수 없습니다"));

            saveAuthentication(member);
        }
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<String> refreshTokenCheck = tokenUtil.extractToken(request, "RefreshToken");

        if (refreshTokenCheck.isEmpty()) {
            return;
        }

        String refreshToken = refreshTokenCheck.get();

        Member member = memberMapper.findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "사용자를 확인할 수 없습니다"));

        // 사용자의 refreshToken이 정확하다면 accessToken과 refreshToken 다시 제작
        String accessToken = tokenService.createAccessToken(member.getMemberEmail());
        String resetRefreshToken = tokenService.createRefreshToken(member.getMemberEmail());

        // 사용자의 refreshToken을 다시 저장하고 AccessToken과 refreshToken을 다시 전달한다
        memberMapper.saveRefreshToken(resetRefreshToken, member.getMemberEmail());

        MemberResponseDTO memberResponse = MemberResponseDTO.from(member);
        tokenUtil.sendTokens(response, accessToken, resetRefreshToken, memberResponse);

        saveAuthentication(member);
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
}
