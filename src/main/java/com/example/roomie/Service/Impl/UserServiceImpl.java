package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.UserOtherDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Entity.Self;
import com.example.roomie.Entity.User;
import com.example.roomie.Entity.UserCharacter;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.SelfRepository;
import com.example.roomie.Repository.UserCharacterRepository;
import com.example.roomie.Repository.UserRepository;
import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserCharacterRepository userCharacterRepository;
    private final SelfRepository selfRepository;

    /**
     * 사용자의 정보를 수정할 때 사용하는 메소드
     * 사용자의 정보가 업데이트 됨 (회원 정보 수정 - 회원가입)
     * @param userSingUpDTO 사용자 정보
     * @param authHeader access token
     * @return 실패/성공 여부
     */
    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader) {

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

    /**
     * 사용자의 특성 및 자기소개 값을 업데이트 함
     * @param userOtherDTO 사용자의 특성값 및 자기소개 값을 저장하는 DTO
     * @param authHeader access token
     * @return 실패/성공 여부
     */
    public Map<String, Object> saveUserInfoOther(UserOtherDTO userOtherDTO, String authHeader) {
        Map<String, Object> token = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        Long userId = jwtService.accessTokenToId(accessToken);

        if(userId == -1) {
            token.put("success", false);
            token.put("message", "Invalid access token");
            return token;
        }

        try {
            // 특성값 저장
            List<UserCharacter> userCharacters = userOtherDTO.getFeatures().stream()
                    .map(characterId -> UserCharacter.builder()
                            .userId(userId)
                            .ucCharacter(Character.builder().characterId(characterId).build())
                            .build()
                    )
                    .toList();
            userCharacterRepository.saveAll(userCharacters);

            // 자기소개 저장
            String selfIntroduction = userOtherDTO.getSelf();
            if (selfIntroduction != null && !selfIntroduction.isEmpty()) {
                Self self = Self.builder()
                        .userId(userId)
                        .aboutMe(selfIntroduction)
                        .build();

                selfRepository.save(self);
            }
        } catch (Exception e) {
            token.put("success", false);
        }

        token.put("success", true);
        return token;
    }

    /**
     * 사용자 탈퇴 처리 -> del_yn 컬럼의 값을 Y로 변경
     * @param authHeader : access token 값
     * @return token
     */
    public Map<String, Object> deleteUser(String authHeader) {
        Map<String, Object> token = new HashMap<>();

        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        Long userId = jwtService.accessTokenToId(accessToken);

        if(userId == -1) {
            token.put("success", false);
            token.put("message", "Invalid access token");
            return token;
        }

        // 값 삭제 ->
//        userRepository.deleteById(userId);
        int result = userRepository.updateUserDelYn(userId);

        if (result > 0) {
            token.put("success", true);
            token.put("accessToken", null);
        } else {
            token.put("success", false);
            token.put("accessToken", accessToken);
        }
        return token;
    }

}
