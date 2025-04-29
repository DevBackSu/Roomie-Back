package com.example.roomie.Repository;

import com.example.roomie.Entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    @Modifying  // query는 기본적으로 select만 가능한 조회 전용 -> 데이터 변경 쿼리 실행 시 필요
    @Transactional // jpa는 자동으로 값을 변경하지 않아서 필요
    @Query("DELETE FROM UserCharacter uc WHERE uc.userId = :userId")
    int deleteByUserId(Long userId);
}
