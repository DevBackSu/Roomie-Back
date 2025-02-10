package com.example.roomie.Repository;

import com.example.roomie.Entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

}
