package com.example.roomie.Repository;

import com.example.roomie.DTO.PostDTO;
import com.example.roomie.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // write_dtm을 기준으로 내림차순 정렬을 한 전체 데이터를 가지고 옴
    // jpa에서 지원하는 페이징 기능 사용
    // 이걸로 조회도 가능한데 지금은 조건을 가진 pageable 객체를 만들고 findAll(pageable)로 조회해서 사용 X
//    List<Post> findAllByOrderByWriteDtmDesc(Pageable pageable);

    @Query("SELECT new com.example.roomie.DTO.PostDTO(p.postId, p.userId, p.title, p.content, p.writeDtm, u.nickname) " +
            "FROM Post p JOIN User u ON p.userId = u.id " +
            "WHERE p.postCheckId = :postCheckId")
    PostDTO findPostDetailByCheckId(@Param("postCheckId") Long postCheckId);
}
