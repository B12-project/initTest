package com.b12.inittest.domain.user.controller;

import com.b12.inittest.common.response.BasicResponse;
import com.b12.inittest.common.security.UserDetailsImpl;
import com.b12.inittest.domain.user.dto.SignUpRequestDto;
import com.b12.inittest.domain.user.dto.SignUpResponseDto;
import com.b12.inittest.domain.user.dto.UserProfileReadResponseDto;
import com.b12.inittest.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/signup")
    public ResponseEntity<BasicResponse<SignUpResponseDto>> signUp(@RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", responseDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<BasicResponse<UserProfileReadResponseDto>> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserProfileReadResponseDto responseDto = userService.getUserProfile(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("프로필 조회가 완료되었습니다.", responseDto));
    }

}
