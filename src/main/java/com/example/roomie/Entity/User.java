package com.example.roomie.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor //builder 패턴을 사용할 때 매개변수가 없는 생성자 외 생성자가 존재할 수 있어서 모든 필드를 가진 생성자도 필요함
@Entity
@Builder // 빌더 패턴 적용
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)  // 소셜 로그인으로 반환되는 값 중 하나인 이메일을 통해 최초 로그인 여부를 파악
    private String email;

    @Column
    private String nickname;

    @Column
    private String gender;

    @Column(name = "main_animal")
    private int mainAnimal; // 추가 정보 | 1 : 아침형 (종달새) / 2 : 저녁형 (올빼미)

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // 추가 정보 (

    @Column
    private String school; // 추가 정보 (캠퍼스 필요 X -> 지역으로 대체)

    @Column
    private String local;  // 추가 정보 (지역)

    @Column
    private String imgUrl; // 추가 정보 (프로필 이미지)

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialToken;

    @Column
    private String refreshToken; // JWT 사용 시 발행된 액세스 토큰과 리프레시 토큰 중 리프레시 토큰을 저장함

    @Getter
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;      // 최초 로그인인지 구분하기 위함 (guest - 최초 로그인 / user - 로그인 기록 존재 / admin - 관리자)

    public String toString() {
        return "id : " + id + "\nnickname : " + nickname + "\ngender : " + gender + "\nbirth_date : " + birthDate + "\nschool : " + school + "\nlocal : " + local +
                "\nimg : " + imgUrl + "\nemail : " + email + "\nrefreshToken : " + refreshToken + "\nsocialType : " + socialType + "\nsocialToken : " + socialToken;
    }


    public void updateRefreshToken(String updateRefreshToken) {  // 리프레시 토큰 재발급
        this.refreshToken = updateRefreshToken;
    }

}
