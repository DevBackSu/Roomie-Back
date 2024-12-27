package com.example.roomie.Repository;

import com.example.roomie.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainRepository  extends JpaRepository<User, Long> {
    List<Integer> findByMainAnimal();
}

interface MainAnimalInfo {
    List<Integer> getMainAnimals();
}