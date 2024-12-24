package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.UserPageDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader) {
        Map<String, Object> token = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);

        // access token 검증
        Long userId = accessTokenToId(accessToken);
        // refresh token 생성 -> token에 추가

        String newRefreshToken = jwtService.createRefreshToken();


        userSingUpDTO.setUserId(userId);
        userSingUpDTO.setRole("USER");
        userSingUpDTO.setRefreshToken(newRefreshToken);

        // User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 필요한 필드만 업데이트
        userSingUpDTO.updateUserEntity(user, newRefreshToken);

        // 데이터 저장
        userRepository.save(user);

        token.put("success", true);
        token.put("refreshToken", newRefreshToken);
        token.put("accessToken", accessToken);

        return token;
    }

    public Map<String, Object> getUserInfo(String authHeader) {
        Map<String, Object> param = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);

        // access token 검증 및 id 반환
        Long userId = accessTokenToId(accessToken);

        Optional<User> user = userRepository.findById(userId);

        UserPageDTO userPageDTO = UserPageDTO.fromEntity(user.get());

        param.put("success", true);
        param.put("userData", userPageDTO);

        return param;

    }

    /**
     * access token 검사 후 user id 반환
     * @param accessToken : access token
     * @return user_id
     */
    private Long accessTokenToId(String accessToken) {
        if (!jwtService.isTokenValid(accessToken)) {
            throw new IllegalArgumentException("Invalid Access Token");
        }

        // access token에서 id값 추출 (Optinal 반환 시 오류 반환)
        Long userId = jwtService.extractId(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Access token is missing or invalid"));

        return userId;
    }

}
