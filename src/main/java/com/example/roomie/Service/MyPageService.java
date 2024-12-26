package com.example.roomie.Service;

import com.example.roomie.DTO.UserPageDTO;

import java.util.Map;

public interface MyPageService {
    Map<String, Object> getUserInfo(String authHeader);
    Map<String, Object> saveUserInfo(UserPageDTO userPageDTO, String authHeader);

}
