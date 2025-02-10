package com.example.roomie.Repository;

import com.example.roomie.Entity.Self;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelfRepository extends JpaRepository<Self, Long> {
    Optional<Self> findByUserId(Long userId);
}
