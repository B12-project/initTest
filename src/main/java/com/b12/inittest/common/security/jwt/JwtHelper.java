package com.b12.inittest.common.security.jwt;

import com.b12.inittest.common.exception.errorCode.SecurityErrorCode;
import com.b12.inittest.common.security.exception.CustomSecurityException;
import com.b12.inittest.common.security.jwt.enums.TokenTime;
import com.b12.inittest.domain.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.b12.inittest.common.security.jwt.JwtConstants.*;

@Slf4j(topic = "JwtHelper")
@Component
@RequiredArgsConstructor
public class JwtHelper {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 헤더에서 토큰값 가져오기
     */
    public String getTokenFromHeader(HttpServletRequest request, String tokenType) {
        String bearerToken = request.getHeader(tokenType);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_FIX)) {
            return bearerToken.substring(7);
        } else {
            log.error("Token이 존재하지 않습니다.");
            request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.JWT_NOT_FOUND));
            return null;
        }
    }

    /**
     * 액세스 토큰 생성
     */
    public String createAccessToken(String userEmail, UserRole userRole) {
        TokenTime tokenTime = TokenTime.getValidTokenTime(userRole);
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + tokenTime.getAccessTokenTime());

        return BEARER_FIX + Jwts.builder()
                .setSubject(userEmail)
                .claim(TOKEN_TYPE, ACCESS_TOKEN_TYPE)
                .claim(USER_AUTH_KEY, userRole)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * 리프레쉬 토큰 생성
     */
    public String createRefreshToken(String userEmail, UserRole userRole) {
        TokenTime tokenTime = TokenTime.getValidTokenTime(userRole);
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() + tokenTime.getRefreshTokenTime());

        return BEARER_FIX + Jwts.builder()
                .setSubject(userEmail)
                .claim(TOKEN_TYPE, REFRESH_TOKEN_TYPE)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.", e);
            request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.INVALID_JWT_TOKEN));
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token입니다.", e);
            request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.EXPIRED_JWT_TOKEN));
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.", e);
            request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.INVALID_JWT_TOKEN));
//            throw new CustomSecurityException(SecurityErrorCode.INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.", e);
            request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.INVALID_JWT_TOKEN));
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
    }

    // 토큰 추출
    // 토큰 생성
    // 토큰 검증
    // 토큰 블랙리스트 추가

}
