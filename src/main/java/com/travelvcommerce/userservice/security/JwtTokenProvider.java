package com.travelvcommerce.userservice.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.token-expiry}")
    private Long jwtTokenExpiry;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String createToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtTokenExpiry);
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        redisTemplate.opsForValue().set(refreshToken, username, jwtTokenExpiry, TimeUnit.MILLISECONDS);
        return refreshToken;
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
            if(redisTemplate.opsForValue().get(token) == null) {
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
