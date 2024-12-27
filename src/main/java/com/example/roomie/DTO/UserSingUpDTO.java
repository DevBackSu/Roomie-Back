package com.example.roomie.DTO;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;

@NoArgsConstructor
@Getter
@Setter
public class UserSingUpDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String gender;
    private int mainAnimal;
    private String birthDate;
    private String school;
    private String local;
    private String imgUrl;
    private String refreshToken;
    private String role;

    @Override
    public String toString() {
        return "userId : " + userId + "\nemail : " + email + "\nnickname : " + nickname + "\ngender : " + gender + "\nmainAnimal : " + mainAnimal +
                "\nbirthDate : " + birthDate + "\nschool : " + school + "\nlocal : " + local + "\nimgUrl : " + imgUrl;
    }

    // UserSingUpDTO 데이터를 기반으로 User 엔티티를 업데이트하는 메서드
    public void updateUserEntity(User user) {
        if (this.nickname != null) user.setNickname(this.nickname);
        if (this.email != null) user.setEmail(this.email);
        if (this.gender != null) user.setGender(this.gender);
        if (this.mainAnimal != 0) user.setMainAnimal(this.mainAnimal);
        if (this.school != null) user.setSchool(this.school);
        if (this.local != null) user.setLocal(this.local);
        if (this.birthDate != null) user.setBirthDate(YearMonth.parse(this.birthDate));
        if (this.imgUrl != null) user.setImgUrl(this.imgUrl);
        if (this.refreshToken != null) user.setRefreshToken(this.refreshToken);
        user.setRole(Role.valueOf(this.role));
    }
}
