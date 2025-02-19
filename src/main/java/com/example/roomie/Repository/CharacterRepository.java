package com.example.roomie.Repository;

import com.example.roomie.Entity.Characters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Characters, Long> {

}
