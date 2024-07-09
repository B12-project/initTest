package com.b12.inittest.global.security;

import com.b12.inittest.global.exception.errorCode.ErrorCode;
import com.b12.inittest.global.response.BasicResponse;
import com.b12.inittest.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response) throws IOException {
        BasicResponse successResponse = BasicResponse.of("로그인에 성공했습니다.");
        String body = objectMapper.writeValueAsString(successResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(body);
    }

    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        String body = objectMapper.writeValueAsString(errorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatusCode());
        response.getWriter().write(body);
    }
}
