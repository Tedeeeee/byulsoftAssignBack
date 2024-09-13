package project.assign.security.service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

// 토큰 제작 서비스
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.secret.key}")
    private String secretKey;
    public static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    public String createAccessToken(String memberEmail) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(memberEmail))
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(createAccessTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public String createRefreshToken(String memberEmail) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(memberEmail))
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(createRefreshTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Map<String, String> createClaims(String memberEmail) {
        Map<String, String> claims = new HashMap<>();

        claims.put("memberEmail", memberEmail);
        return claims;
    }

    private Date createAccessTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 6);
        return c.getTime();
    }

    private Date createRefreshTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }

    public Key createSignature() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
