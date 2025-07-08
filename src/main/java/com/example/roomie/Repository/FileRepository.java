package com.example.roomie.Repository;

import com.example.roomie.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
