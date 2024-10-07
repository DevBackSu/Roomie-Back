package com.example.roomie.Repository;

import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByNicname(String nicname); // 별명 찾기
    Optional<User> findByRefreshToken(String refreshToken); // 리프레시 토큰 찾기

    /**
     * 소셜 로그인 후 추가 정보 insert를 위한 사용자 select 메소드
     */
    Optional<User> findBySocialTypeAndSocialToken(SocialType socialType, String socialToken);
}
