package com.example.roomie.Service.Impl;

import com.auth0.jwt.interfaces.Claim;
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

        System.out.println("\n\n\n-----------------------\n");
        System.out.println(accessToken);
        System.out.println("\n-----------------------\n\n\n");

        if(!jwtService.isTokenValid(accessToken)) {
            throw new IllegalArgumentException("Invalid Access Token");
        }

        Long userId = jwtService.extractId(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("Access token is missing or invalid"));
//        Optional<Long> userId = jwtService.extractId(accessToken);
//        Long user_Id = jwtService.extractId(accessToken).get();

        System.out.println(userId);
//        System.out.println(user_Id);

//        if(userId.isEmpty()) {
//            throw new IllegalArgumentException("access token check!");
//        }



        userSingUpDTO.setUserId(Long.valueOf(String.valueOf(userId)));
        userSingUpDTO.setRole("USER");


        token.put("success", true);



        return token;
    }

}
