package com.b12.inittest.global.security.jwt;

import com.b12.inittest.global.exception.errorCode.SecurityErrorCode;
import com.b12.inittest.global.security.ResponseUtil;
import com.b12.inittest.global.security.UserDetailsServiceImpl;
import com.b12.inittest.global.exception.customException.CustomSecurityException;
import com.b12.inittest.global.security.jwt.dto.LoginRequestDto;
import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.entity.UserStatus;
import com.b12.inittest.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.b12.inittest.global.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;
import static com.b12.inittest.global.security.jwt.JwtConstants.REFRESH_TOKEN_HEADER;

@Slf4j(topic = "로그인 및 인증")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e){
            throw new CustomSecurityException(SecurityErrorCode.LOGIN_FAILED);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User loginUser = userDetailsService.findUserByUserName(authResult.getName());

        if(loginUser.getUserStatus().equals(UserStatus.DEACTIVATE)) {
            ResponseUtil.setErrorResponse(response, SecurityErrorCode.USER_DEACTIVATE);
        } else {

            String accessToken = jwtHelper.createAccessToken(loginUser.getEmail(), loginUser.getUserRole());
            String refreshToken = jwtHelper.createRefreshToken(loginUser.getEmail(), loginUser.getUserRole());

            loginUser.updateRefreshToken(refreshToken);
            userRepository.save(loginUser);

            response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
            response.addHeader(REFRESH_TOKEN_HEADER, refreshToken);
            ResponseUtil.setSuccessResponse(response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ResponseUtil.setErrorResponse(response, SecurityErrorCode.EMAIL_PASSWORD_MISMATCH);
    }
}
