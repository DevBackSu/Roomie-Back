package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        System.out.println("user service");
        System.out.println("userSingUpDTO : " + userSingUpDTO);
        System.out.println("authHeader : " + authHeader);
        System.out.println("refreshToken : " + refreshToken);
        System.out.println("\n-----------------------\n\n\n");

        String accessToken = jwtService.extractTokenAccessToken(authHeader);

        if(!jwtService.isTokenValid(accessToken)) {
            throw new IllegalArgumentException("Invalid Access Token");
        }

        Optional<String> userId = jwtService.extractId(accessToken);

        if(userId.isEmpty()) {
            throw new IllegalArgumentException("access token check!");
        }

        userSingUpDTO.setUserId(Long.valueOf(String.valueOf(userId)));
        userSingUpDTO.setRole("USER");


        token.put("success", "true");



        return token;
    }

}
