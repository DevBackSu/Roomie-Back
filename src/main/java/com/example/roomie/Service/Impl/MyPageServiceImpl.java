package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.UserPageDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import com.example.roomie.Service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Map<String, Object> getUserInfo(String authHeader) {
        Map<String, Object> param = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);  // access token 추출
        Long userId = jwtService.accessTokenToId(accessToken); // access token 검증

        if(userId == -1) {  // access token이 유효하지 않을 경우
            param.put("success", false);
            param.put("message", "Invalid access token");
            return param;
        }

        Optional<User> user = userRepository.findById(userId);

        UserPageDTO userPageDTO = UserPageDTO.fromEntity(user.get());

        param.put("success", true);
        param.put("userData", userPageDTO);

        return param;
    }

    public Map<String, Object> saveUserInfo(UserPageDTO userSingUpDTO, String authHeader) {
        Map<String, Object> param = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);  // access token 추출
        Long userId = jwtService.accessTokenToId(accessToken); // access token 검증

        if(userId == -1) {  // access token이 유효하지 않을 경우
            param.put("success", false);
            param.put("message", "Invalid access token");
            return param;
        }

        // User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        userSingUpDTO.setRole("USER");
        userSingUpDTO.setUserId(userId);

        // 필요한 필드만 업데이트
        userSingUpDTO.updateUserEntity(user);

        // 데이터 저장
        userRepository.save(user);

        param.put("success", true);
        param.put("accessToken", accessToken);

        return param;
    }
}
