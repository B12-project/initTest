package com.b12.inittest.global.security.jwt;

import com.b12.inittest.global.exception.errorCode.SecurityErrorCode;
import com.b12.inittest.global.security.UserDetailsServiceImpl;
import com.b12.inittest.global.exception.customException.CustomSecurityException;
import com.b12.inittest.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.b12.inittest.global.security.jwt.JwtConstants.*;

@Slf4j(topic = "JWT 인증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtHelper.getTokenFromHeader(request, ACCESS_TOKEN_HEADER);

        if (StringUtils.hasText(accessToken) && jwtHelper.validateToken(request, accessToken)) {
            String userEmail = jwtHelper.getUserInfoFromToken(accessToken).getSubject();
            User findUser = userDetailsService.findUserByUserName(userEmail);

            // 유저 리프레쉬 토큰값이 데이터베이스에 존재하는 경우
            if (findUser.getRefreshToken() != null) {
                Claims info = jwtHelper.getUserInfoFromToken(accessToken);
                setAuthentication(info.getSubject());
            } else {
                request.setAttribute("exception", new CustomSecurityException(SecurityErrorCode.REFRESH_NOT_FOUND));
            }
        }
        // 액세스 토큰이 만료된 경우...?
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
