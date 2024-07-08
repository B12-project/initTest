package com.b12.inittest.common.security;

import com.b12.inittest.common.exception.errorCode.SecurityErrorCode;
import com.b12.inittest.common.security.exception.CustomSecurityException;
import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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
