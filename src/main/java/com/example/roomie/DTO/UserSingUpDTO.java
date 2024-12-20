package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
