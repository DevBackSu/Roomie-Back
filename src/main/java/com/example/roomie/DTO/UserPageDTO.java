package com.example.roomie.DTO;

import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserPageDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String gender;
    private int mainAnimal;
    private String birthDate;
    private String school;
    private String local;
    private String imgUrl;
    private SocialType socialType;
    private String role;


    // User 엔티티에서 UserPageDTO로 변환하는 정적 메서드
    public static UserPageDTO fromEntity(User user) {
        UserPageDTO dto = new UserPageDTO();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setGender(user.getGender());
        dto.setMainAnimal(user.getMainAnimal());
        dto.setBirthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null);
        dto.setSchool(user.getSchool());
        dto.setLocal(user.getLocal());
        dto.setImgUrl(user.getImgUrl());
        dto.setSocialType(user.getSocialType());
        dto.setRole(user.getRole().toString());
        return dto;
    }
}