package com.example.roomie.Service.Impl;

import com.auth0.jwt.interfaces.Claim;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Entity.Role;
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

    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader, String refreshToken) {
        Map<String, Object> token = new HashMap<>();

        System.out.println("\n\n\n-----------------------\n");
        System.out.println("user service\n");
        System.out.println("userSingUpDTO : " + userSingUpDTO);
        System.out.println("authHeader : " + authHeader);
        System.out.println("refreshToken : " + refreshToken);
        System.out.println("\n-----------------------\n\n\n");

        String accessToken = jwtService.extractTokenAccessToken(authHeader);

        // access token 검증
        if (!jwtService.isTokenValid(accessToken)) {
            throw new IllegalArgumentException("Invalid Access Token");
        }

        // access token에서 id값 추출 (Optinal 반환 시 오류 반환)
        Long userId = jwtService.extractId(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Access token is missing or invalid"));

        // refresh token 생성 -> token에 추가
        String newRefreshToken = jwtService.createRefreshToken();

        System.out.println("\n\n\n--------------------------\n");
        System.out.println("refershToken : " + newRefreshToken);
        System.out.println("\n--------------------------\n\n\n");


        userSingUpDTO.setUserId(userId);
        userSingUpDTO.setRole("USER");
        userSingUpDTO.setRefreshToken(newRefreshToken);

        // User 객체 생성
        User user = User.builder()
                .id(userId)
                .email(userSingUpDTO.getEmail())
                .nickname(userSingUpDTO.getNickname())
                .gender(userSingUpDTO.getGender())
                .mainAnimal(userSingUpDTO.getMainAnimal())
                .birthDate(YearMonth.parse(userSingUpDTO.getBirthDate()))
                .school(userSingUpDTO.getSchool())
                .local(userSingUpDTO.getLocal())
                .imgUrl(userSingUpDTO.getImgUrl())
                .refreshToken(newRefreshToken)
                .role(Role.valueOf(userSingUpDTO.getRole()))
                .build();

        //
        userRepository.save(user);

        token.put("success", true);
        token.put("refreshToken", newRefreshToken);
        token.put("accessToken", accessToken);

        return token;
    }

}
