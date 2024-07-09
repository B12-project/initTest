package com.b12.inittest.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String userName;

}
