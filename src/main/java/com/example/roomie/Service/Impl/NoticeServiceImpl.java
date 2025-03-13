package com.example.roomie.Service.Impl;

import com.example.roomie.Entity.Notice;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.NoticeRepository;
import com.example.roomie.Service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final JwtService jwtService;

    public List<Notice> getNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("writeDtm").descending());
        return noticeRepository.findAllByOrderByWriteDtmDesc(pageable);
    }
}
