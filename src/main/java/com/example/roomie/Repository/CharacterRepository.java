package com.example.roomie.Repository;

import com.example.roomie.Entity.Characters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Characters, Long> {

}
