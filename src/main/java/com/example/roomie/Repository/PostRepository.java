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

    // JPQL은 필드 순서대로 바인딩됨 -> 필드와 select에 작성한 필드 순서가 다르면 오류가 발생할 수 있음
    @Query("SELECT new com.example.roomie.DTO.PostDTO(p.postId, p.user.id, p.postCheckId, p.title, p.content, p.writeDtm, p.user.nickname) " +
            "FROM Post p WHERE p.postCheckId = :postCheckId")
    PostDTO findPostDetailByCheckId(@Param("postCheckId") Long postCheckId);

    boolean existsByPostCheckId(Long postCheckId);

    boolean existsByPostIdAndUser_Id(Long postId, Long userId);
}
