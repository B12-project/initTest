package com.b12.inittest.domain.user.service;

import com.b12.inittest.common.exception.customException.UserException;
import com.b12.inittest.common.exception.errorCode.UserErrorCode;
import com.b12.inittest.domain.user.dto.SignUpRequestDto;
import com.b12.inittest.domain.user.dto.SignUpResponseDto;
import com.b12.inittest.domain.user.dto.UserProfileReadResponseDto;
import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.entity.UserRole;
import com.b12.inittest.domain.user.entity.UserStatus;
import com.b12.inittest.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User newUser = User.saveUserData(requestDto, encodedPassword, UserRole.USER);

        userRepository.save(newUser);
        return SignUpResponseDto.of(newUser);
    }

    @Transactional
    public void logout(User user) {
        user.updateRefreshToken(null);
        userRepository.save(user);
    }

    @Transactional
    public void deactivate(User user) {
        user.deactivateUser();
        user.updateRefreshToken(null);
        userRepository.save(user);
    }

    public UserProfileReadResponseDto getUserProfile(User user) {
        return UserProfileReadResponseDto.of(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND)
        );
    }
}
