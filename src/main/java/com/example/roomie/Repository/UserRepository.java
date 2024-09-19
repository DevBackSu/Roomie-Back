package com.example.roomie.Repository;

import com.example.roomie.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNicname(String nicname); // 별명 찾기
    Optional<User> findByRefreshToken(String refreshToken); // 리프레시 토큰 찾기

    /**
     * 소셜 로그인 후 추가 정보 insert를 위한 사용자 select 메소드
     */
    Optional<User> findByKakaoId(String kakaoId);
    Optional<User> findByGoogleId(String googleId);

    @Query("SELECT CASE " +
            "WHEN u.googleToken IS NOT NULL THEN 'Google' " +
            "WHEN u.kakaoToken IS NOT NULL THEN 'Kakao' " +
            "WHEN u.naverToken IS NOT NULL THEN 'Naver' " +
            "ELSE 'No' " +
            "END " +
            "FROM User u " +
            "WHERE u.id = :userId")
    Optional<User> findUserTokenType(int user_id);
}
