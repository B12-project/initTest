package com.b12.inittest.domain.token.controller;

import com.b12.inittest.domain.token.service.TokenService;
import com.b12.inittest.global.response.BasicResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<BasicResponse<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        tokenService.reissue(request, response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "토큰 재발급이 완료되었습니다."));
    }
}
