package com.example.roomie.Repository;

import com.example.roomie.DTO.CharacterDTO;
import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname); // 별명 찾기
    Optional<User> findByRefreshToken(String refreshToken); // 리프레시 토큰 찾기

    Optional<User> findBySocialToken(String socialToken);

    @Transactional
    @Modifying(clearAutomatically = true) // @Query로 정의된 jpql 실행 후 자동으로 영속성 컨텍스트를 비워줌
    // ㄴ영속성 컨텍스트에 정의된 데이터가 우선 순위를 갖고 덮어쓰지 않기 때문에 실제 변경된 데이터가 영속성 컨텍스트에 저장되지 못하고 기존 데이터가 출력될 수 있음
    @Query("UPDATE User u SET u.delYn = 'Y' WHERE u.id = :userId")
    int updateUserDelYn(@Param("userId") Long userId);

    /**
     * 소셜 로그인 후 추가 정보 insert를 위한 사용자 select 메소드
     */
    Optional<User> findBySocialTypeAndSocialToken(SocialType socialType, String socialToken);

    // 이건 user character repo로 이동하기
    @Query("SELECT c.character " +
            "FROM UserCharacter uc " +
            "JOIN uc.ucCharacter c " +
            "WHERE uc.userId = :userId")
    List<String> findUserCharacter(@Param("userId") Long userId);


    // 이건 self repo로 이동해야 할 듯
    @Query(value = "SELECT s.aboutMe " +
                    "FROM Self s " +
                    "WHERE s.userId = :userId ")
    String findUserSelf(@Param("userId") Long userId);
}