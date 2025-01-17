package com.example.roomie.Repository;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainRepository  extends JpaRepository<User, Long> {
    @Query("SELECT u.mainAnimal FROM User u")
    List<Integer> findMainAnimal();

    @Query("select u.local from User u group by u.local order by count(*) desc limit 5")
    List<String> findLrank();
}

