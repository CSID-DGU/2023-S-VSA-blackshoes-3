package com.travelvcommerce.userservice.security;

import com.travelvcommerce.userservice.dto.TokenDto;
import com.travelvcommerce.userservice.entity.RefreshToken;
import com.travelvcommerce.userservice.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.token-expiry}")
    private Long jwtTokenExpiry;

    @Value("${app.jwt.refresh-token-expiry}")
    private Long jwtRefreshTokenExpiry;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public TokenDto createTokens(String email, String userType) {
        Date now = new Date();
        Date tokenExpiryDate = new Date(now.getTime() + jwtTokenExpiry);
        Date refreshTokenExpiryDate = new Date(now.getTime() + jwtRefreshTokenExpiry);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)  // userType claim 추가
                .setIssuedAt(now)
                .setExpiration(tokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)  // userType claim 추가
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        refreshTokenRepository.save(userType, email, refreshToken, jwtRefreshTokenExpiry);

        return new TokenDto(token, refreshToken);
    }

    public String createAccessToken(String username, String userType) {
        Date now = new Date();
        Date tokenExpiryDate = new Date(now.getTime() + jwtTokenExpiry);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .claim("userType", userType)
                .setExpiration(tokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("유효하지 않은 JWT 서명입니다");
        } catch (MalformedJwtException ex) {
            System.out.println("유효하지 않은 JWT 토큰입니다");
        } catch (ExpiredJwtException ex) {
            System.out.println("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException ex) {
            System.out.println("지원하지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT 토큰이 비어있습니다");
        }
        return false;
    }
    public String getUserTypeFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userType", String.class);
    }
}
