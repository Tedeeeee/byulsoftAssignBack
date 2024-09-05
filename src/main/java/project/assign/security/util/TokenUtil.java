package project.assign.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import project.assign.security.service.TokenService;

import java.io.IOException;
import java.security.Key;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    // 토큰 전달
    public void sendAccessToken(HttpServletResponse response, String accessToken, String nickname) throws IOException {
        ResponseCookie cookie = ResponseCookie.from(TokenService.ACCESS_TOKEN_SUBJECT, accessToken)
                .secure(false)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String userNickname = objectMapper.writeValueAsString(nickname);
        response.getWriter().write(userNickname);

        log.info("Access token 데이터 전송 완료: {}", accessToken);
    }

    // 토큰 해석하기
    private Optional<Claims> getClaimsFromToken(String token) {
        Key key = tokenService.createSignature();
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return Optional.ofNullable(body);
    }

    // 토큰에서 사용자 이메일 추출
    public String getMemberEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token)
                .orElseThrow(() -> new RuntimeException("토큰에 유저의 정보가 없습니다"));

        return claims.get("memberEmail").toString();
    }

    // request 토큰 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> TokenService.ACCESS_TOKEN_SUBJECT.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
        }
        return Optional.empty();
    }

    // 토큰 유효성 검증
    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token)
                    .orElseThrow(() -> new RuntimeException("토큰이 존재하지 않습니다"));

            log.info("expireTime : {}", claims.getExpiration());
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("만료된 JWT 토큰입니다");
            return false;
        } catch (JwtException exception) {
            log.error("토큰에 문제가 있습니다.");
            return false;
        }
    }
}
