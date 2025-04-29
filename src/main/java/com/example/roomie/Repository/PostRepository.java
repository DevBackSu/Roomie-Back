package com.example.roomie.Repository;

import com.example.roomie.Entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // write_dtm을 기준으로 내림차순 정렬을 한 전체 데이터를 가지고 옴
    // jpa에서 지원하는 페이징 기능 사용
    List<Post> findAllByOrderByWriteDtmDesc(Pageable pageable);
}
