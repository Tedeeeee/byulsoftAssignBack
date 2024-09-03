package project.assign.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String BEARER = "Bearer ";

    public String createAccessToken(String memberEmail) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(memberEmail))
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(createAccessTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private static Map<String, String> createClaims(String memberUUID) {
        Map<String, String> claims = new HashMap<>();

        claims.put("memberUUID", memberUUID);
        return claims;
    }

    private static Date createAccessTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);
        return c.getTime();
    }

    private Key createSignature() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 해석하기
    private Claims getClaimsFromToken(String token) {
        Key key = createSignature();
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        if (body != null) {
            log.info("토큰 해석 성공! : {}", body);
            return body;
        } else {
            log.info("토큰 해석 실패");
            return null;
        }
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("AccessToken", accessToken);

        log.info("Access token 데이터 전송 완료: {}", accessToken);
    }

    public String getMemberEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("memberEmail").toString();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("AccessToken"))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            log.info("expireTime : " + claims.getExpiration());

            return true;
        } catch (ExpiredJwtException exception) {
            log.error("만료된 JWT 토큰입니다");
            return false;
        } catch (JwtException exception) {
            log.error("토큰에 문제가 있습니다.");
            return false;
        } catch (NullPointerException exception) {
            log.error("토큰이 존재하지 않습니다");
            return false;
        }
    }
}
