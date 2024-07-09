package com.b12.inittest.global.security.jwt;

import com.b12.inittest.global.exception.errorCode.CommonErrorCode;
import com.b12.inittest.global.security.ResponseUtil;
import com.b12.inittest.global.exception.customException.CustomSecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "예외 필터")
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            log.info(request.getRequestURI());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof CustomSecurityException exception) {
                ResponseUtil.setErrorResponse(response, exception.getErrorCode());
                return;
            }
            ResponseUtil.setErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

//    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws JsonProcessingException {
//        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
//        String body = objectMapper.writeValueAsString(errorResponse);
//
//        response.setContentType("application/json;c");
//    }
}
