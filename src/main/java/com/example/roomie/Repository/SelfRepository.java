package com.example.roomie.Repository;

import com.example.roomie.Entity.Self;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SelfRepository extends JpaRepository<Self, Long> {

//    Optional<Self> findByUserId(Long userId);

    /**
     * 사용자 id에 해당하는 자기소개 데이터를 조회함
     * @param userId
     * @return
     */
    @Query(value = "SELECT s.aboutMe " +
            "FROM Self s " +
            "WHERE s.userId = :userId ")
    String findUserSelf(@Param("userId") Long userId);

    /**
     * 사용자 id에 해당하는 자기소개 데이터를 수정함
     * @param userId
     * @param aboutMe
     * @return
     */
    @Modifying // delete로 데이터에 변경이 발생함 -> modifying 필요
    @Transactional
    @Query("UPDATE Self s SET s.aboutMe = :aboutMe WHERE s.userId = :userId")
    int updateAboutMe(Long userId, String aboutMe);
}
