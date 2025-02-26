package com.example.roomie.DTO;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
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
    private String socialToken;
    private String refreshToken;
    private String role;
    private String delYn;

    public static UserDTO fromEntity(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setGender(user.getGender());
        userDTO.setMainAnimal(user.getMainAnimal());
        userDTO.setBirthDate(user.getBirthDate() != null ? user.getBirthDate().toString() : null);
        userDTO.setSchool(user.getSchool());
        userDTO.setLocal(user.getLocal());
        userDTO.setImgUrl(user.getImgUrl());
        userDTO.setSocialType(user.getSocialType());
        userDTO.setSocialToken(user.getSocialToken());
        userDTO.setRefreshToken(user.getRefreshToken());
        userDTO.setRole(user.getRole().toString());
        userDTO.setDelYn(user.getDelYn());
        return userDTO;
    }

    public void deleteUserEntity(User user) {
        if (this.userId != null) { user.setId(this.userId); }
        if (this.email != null) user.setEmail(this.email);
        if (this.nickname != null) user.setNickname(this.nickname);
        if (this.gender != null) user.setGender(this.gender);
        if (this.mainAnimal != 0) user.setMainAnimal(this.mainAnimal);
        if (this.birthDate != null) user.setBirthDate(YearMonth.parse(this.birthDate));
        if (this.school != null) user.setSchool(this.school);
        if (this.local != null) user.setLocal(this.local);
        if (this.imgUrl != null) user.setImgUrl(this.imgUrl);
        if (this.socialType != null) user.setSocialType(this.socialType);
        if (this.socialToken != null) user.setSocialToken(this.socialToken);
        if (this.refreshToken != null) user.setRefreshToken(this.refreshToken);
        user.setRole(Role.valueOf(this.role));
        user.setDelYn(this.delYn);
    }
}
