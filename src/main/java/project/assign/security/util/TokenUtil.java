package project.assign.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import project.assign.commonApi.CommonResponse;
import project.assign.dto.MemberResponseDTO;
import project.assign.exception.BusinessExceptionHandler;
import project.assign.security.service.TokenService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {
    private final TokenService tokenService;

    // 토큰 전달
    public void sendTokens(HttpServletResponse response, String accessToken, String refreshToken, MemberResponseDTO memberResponseDTO) throws IOException {
        ResponseCookie accessCookie = ResponseCookie.from(TokenService.ACCESS_TOKEN_SUBJECT, accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(TokenService.REFRESH_TOKEN_SUBJECT, refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(604800)
                .build();

        // 한글의 닉네임인 경우 비 ASCII 문자이기 때문에 오류 발생으로 한글을 읽을 수 있도록 변경
        String encodedNickname = URLEncoder.encode(memberResponseDTO.getMemberNickname(), StandardCharsets.UTF_8);
        ResponseCookie nicknameCookie = ResponseCookie.from("nickname", encodedNickname)
                .path("/")
                .maxAge(3600)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, nicknameCookie.toString());

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
                .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.UNAUTHORIZED, 401,  "토큰이 존재하지 않습니다"));

        return claims.get("memberEmail").toString();
    }

    // request 토큰 추출
    public Optional<String> extractToken(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> tokenName.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
        }
        return Optional.empty();
    }

    // 토큰 유효성 검증
    public boolean isValidToken(String token) {
        try {
            getClaimsFromToken(token)
                    .orElseThrow(() -> new BusinessExceptionHandler(HttpStatus.UNAUTHORIZED, 401,  "토큰이 존재하지 않습니다"));

            return true;
        } catch (JwtException exception) {
            log.error(exception.getMessage());
            throw new BusinessExceptionHandler(HttpStatus.UNAUTHORIZED, 401, "토큰이 유효하지 않거나 존재하지 않습니다");
        }
    }
}
