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
@Table(name="USER")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;

        private String nickname;

        private String gender;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birth_date;

        private String school;
        private String campus; //캠퍼스 정보 (필요한지 더 고민 필요)

        private String email;

        private String googleToken;
        private String kakaoToken;
        private String naverToken;

        private String refreshToken; // JWT 사용 시 발행된 액세스 토큰과 리프레시 토큰 중 리프레시 토큰을 저장함

        public String toString(){
            return "id : " + id + "\nnickname : " + nickname + "\ngender : " + gender + "\nbirth_date : " + birth_date + "\nschool : " + school + "\nemail : " + email +
                     "\nrefreshToken : " + refreshToken + "\ngoogleToken : " + googleToken + "\nkakaoToken : " + kakaoToken + "\nnaverToken : " + naverToken;
        }

        public void updateRefreshToken(String updateRefreshToken) {  // 리프레시 토큰 재발급
            this.refreshToken = updateRefreshToken;
        }

}
