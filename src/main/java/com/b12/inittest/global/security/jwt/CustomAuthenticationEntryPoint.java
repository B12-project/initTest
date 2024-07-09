package com.b12.inittest.global.security.jwt;

import com.b12.inittest.global.exception.errorCode.CommonErrorCode;
import com.b12.inittest.global.security.ResponseUtil;
import com.b12.inittest.global.exception.customException.CustomSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "인증 예외 처리")
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        Exception exception = (Exception) request.getAttribute("exception");

        if (exception instanceof CustomSecurityException e) {
            ResponseUtil.setErrorResponse(response, e.getErrorCode());
            return;
        }

        ResponseUtil.setErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
