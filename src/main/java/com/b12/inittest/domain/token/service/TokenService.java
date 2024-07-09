package com.b12.inittest.domain.token.service;

import com.b12.inittest.domain.user.entity.User;
import com.b12.inittest.domain.user.repository.UserRepository;
import com.b12.inittest.domain.user.service.UserService;
import com.b12.inittest.global.exception.customException.TokenException;
import com.b12.inittest.global.exception.customException.UserException;
import com.b12.inittest.global.exception.errorCode.TokenErrorCode;
import com.b12.inittest.global.exception.errorCode.UserErrorCode;
import com.b12.inittest.global.security.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.b12.inittest.global.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;
import static com.b12.inittest.global.security.jwt.JwtConstants.REFRESH_TOKEN_HEADER;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtHelper.getTokenFromHeader(request, REFRESH_TOKEN_HEADER);

        if (jwtHelper.validateToken(request, refreshToken)) {
            String userEmail = jwtHelper.getUserInfoFromToken(refreshToken).getSubject();
            User findUser = userRepository.findByEmail(userEmail).orElseThrow(() ->
                        new UserException(UserErrorCode.USER_NOT_FOUND)
                    );

            String newAccessToken = jwtHelper.createAccessToken(findUser.getEmail(), findUser.getUserRole());
            String newRefreshToken = jwtHelper.createRefreshToken(findUser.getEmail(), findUser.getUserRole());
            findUser.updateRefreshToken(newRefreshToken);

            response.addHeader(ACCESS_TOKEN_HEADER, newAccessToken);
            response.addHeader(REFRESH_TOKEN_HEADER, newRefreshToken);

        } else {
            throw new TokenException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
