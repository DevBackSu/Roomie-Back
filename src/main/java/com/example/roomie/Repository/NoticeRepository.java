package com.example.roomie.Repository;

import com.example.roomie.Entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    // write_dtm을 기준으로 내림차순 정렬을 한 전체 데이터를 가지고 옴
    // jpa에서 지원하는 페이징 기능 사용
    List<Notice> findAllByOrderByWriteDtmDesc(Pageable pageable);
}
