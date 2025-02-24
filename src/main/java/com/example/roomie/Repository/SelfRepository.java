package com.example.roomie.Repository;

import com.example.roomie.Entity.Self;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SelfRepository extends JpaRepository<Self, Long> {
    Optional<Self> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Self s SET s.aboutMe = :aboutMe WHERE s.userId = :userId")
    int updateAboutMe(Long userId, String aboutMe);
}
