package com.example.roomie.Service;

import com.example.roomie.DTO.UserSingUpDTO;

import java.util.Map;

public interface UserService {

    Map<String, Object> saveUserInfo(UserSingUpDTO userSingUpDTO, String authHeader);

    Map<String, Object> deleteUser(String authHeader);
}
