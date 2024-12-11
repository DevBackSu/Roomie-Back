package com.example.roomie.Service;

import com.example.roomie.DTO.UserSingUpDTO;

import java.util.Map;

public interface UserService {

    public Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader, String refreshToken);
}
