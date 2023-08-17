package com.travelvcommerce.userservice.security;
import com.travelvcommerce.userservice.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//HTTP 요청에서 JWT 토큰을 검색하고, 토큰이 유효한지 확인합니다.
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Qualifier("sellerDetailsService")
    @Autowired
    private UserDetailsService sellerDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = getToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            String email = tokenProvider.getEmailFromToken(token);
            String userType = tokenProvider.getUserTypeFromToken(token);  // userType을 가져옵니다.
            String userId = tokenProvider.getUserIdFromToken(token);  // userId를 가져옵니다.

            UserDetails userDetails;
            if (userType.equals(Role.SELLER.getRoleName())) {
                userDetails = sellerDetailsService.loadUserByUsername(email);
            } else if (userType.equals(Role.USER.getRoleName())){
                userDetails = userDetailsService.loadUserByUsername(email);
            } else {
                throw new IllegalArgumentException("유효하지 않은 사용자 타입입니다.");
            }

            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    AuthorityUtils.NO_AUTHORITIES
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }



}
