package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.UserPageDTO;
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

    private static final String INVALID_ACCESS_TOKEN_MSG = "Invalid access token";
    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        return response;
    }

    private Long validateAccessToken(String authHeader) {
        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        return jwtService.accessTokenToId(accessToken);
    }

    @Override
    public Map<String, Object> getUserInfo(String authHeader) {
        Long userId = validateAccessToken(authHeader);
        if (userId == -1) {
            return createErrorResponse(INVALID_ACCESS_TOKEN_MSG);
        }

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return createErrorResponse(USER_NOT_FOUND_MSG + userId);
        }

        UserPageDTO userPageDTO = UserPageDTO.fromEntity(user.get());
        return createSuccessResponse(userPageDTO);
    }

    @Override
    public Map<String, Object> saveUserInfo(UserPageDTO userPageDTO, String authHeader) {
        Long userId = validateAccessToken(authHeader);
        if (userId == -1) {
            return createErrorResponse(INVALID_ACCESS_TOKEN_MSG);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MSG + userId));

        // DTO를 User 엔티티로 변환하여 업데이트
        userPageDTO.setRole("USER");
        userPageDTO.setUserId(userId);
        userPageDTO.updateUserEntity(user);

        userRepository.save(user);
        return createSuccessResponse(userId);
    }
}
