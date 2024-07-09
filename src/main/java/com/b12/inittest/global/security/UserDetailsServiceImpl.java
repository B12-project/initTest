package com.b12.inittest.global.security;

import com.b12.inittest.global.exception.errorCode.SecurityErrorCode;
import com.b12.inittest.global.exception.customException.CustomSecurityException;
import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUserName(username);
        return new UserDetailsImpl(user);
    }

    public User findUserByUserName(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new CustomSecurityException(SecurityErrorCode.USER_NOT_FOUND));
    }
}
