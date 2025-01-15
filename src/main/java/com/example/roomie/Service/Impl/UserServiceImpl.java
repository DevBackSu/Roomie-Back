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

    /**
     * 사용자의 정보를 수정할 때 사용하는 메소드
     * 사용자의 정보가 업데이트 됨 (회원 정보 수정 - 회원가입)
     * @param userSingUpDTO
     * @param authHeader
     * @return
     */
    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader) {
        System.out.println("\n\n\n-------------------------------\n");
        System.out.println("사용자 정보 수정 메소드 진입");
        System.out.println("\n-------------------------------\n\n\n");

        Map<String, Object> token = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);  // access token 추출
        Long userId = jwtService.accessTokenToId(accessToken); // access token 검증

        if(userId == -1) {  // access token이 유효하지 않을 경우
            token.put("success", false);
            token.put("message", "Invalid access token");
            return token;
        }

        String newRefreshToken = jwtService.createRefreshToken();             // refresh token 생성

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
