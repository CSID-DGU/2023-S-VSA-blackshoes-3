package com.travelvcommerce.userservice.security;

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

    public Map<String, String> createTokens(String username) {
        Date now = new Date();
        Date tokenExpiryDate = new Date(now.getTime() + jwtTokenExpiry);
        Date refreshTokenExpiryDate = new Date(now.getTime() + jwtRefreshTokenExpiry);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(tokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setId(refreshToken);
        tokenEntity.setUsername(username);

        refreshTokenRepository.save(tokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", token);
        tokens.put("refreshToken", refreshToken);
        return tokens;
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
            if(refreshTokenRepository.findById(token).isEmpty()) {
                throw new ExpiredJwtException(null, null, "Refresh token has expired");
            }
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
}
