package com.b12.inittest.domain.user.service;

import com.b12.inittest.domain.user.dto.SignUpRequestDto;
import com.b12.inittest.domain.user.dto.SignUpResponseDto;
import com.b12.inittest.domain.user.dto.UserProfileReadResponseDto;
import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.entity.UserRole;
import com.b12.inittest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = User.saveUserData(requestDto, encodedPassword, UserRole.USER);

        userRepository.save(newUser);
        return SignUpResponseDto.of(newUser);
    }

    public UserProfileReadResponseDto getUserProfile(User user) {
        return UserProfileReadResponseDto.of(user);
    }
}
