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

import static com.example.roomie.Entity.Role.GUEST;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader) {
        Map<String, Object> token = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        String newRefreshToken = jwtService.createRefreshToken();

        // access token 검증
        Long userId = jwtService.accessTokenToId(accessToken);

        userSingUpDTO.setUserId(userId);

        // User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        userSingUpDTO.setRefreshToken(newRefreshToken);
        userSingUpDTO.setRole("USER");

        // 필요한 필드만 업데이트
        userSingUpDTO.updateUserEntity(user);

        // 데이터 저장
        userRepository.save(user);

        token.put("success", true);
        token.put("refreshToken", newRefreshToken);
        token.put("accessToken", accessToken);

        return token;
    }

}
