package com.example.roomie.Repository;

import com.example.roomie.DTO.FileDTO;
import com.example.roomie.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * PostCheckId에 해당하는 첨부파일 리스트 조회
     * @param postCheckId
     * @return
     */
    @Query("SELECT new com.example.roomie.DTO.FileDTO(f.fileId, f.post.postId, f.filePath, f.fileName, f.originName, f.fileType) " +
            "FROM File f WHERE f.post.postCheckId = :postCheckId")
    List<FileDTO> findFileDTOsByPostCheckId(@Param("postCheckId") Long postCheckId);
}
