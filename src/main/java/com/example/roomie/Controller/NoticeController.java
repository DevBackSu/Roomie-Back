package com.example.roomie.Controller;

import com.example.roomie.Entity.Notice;
import com.example.roomie.Service.NoticeService;
import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    /**
     * 전체 게시글 목록 조회
     * JPA의 Pageable을 사용해 페이지네이션 함
     *
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getNoticeList(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getNoticeList 접근");
        Map<String, Object> response = new HashMap<>();

        try {
            List<Notice> noticeList = noticeService.getNoticeList(page, size);

            response.put("success", "true");
            response.put("noticeList", noticeList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.put("success", "false");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 게시글 조회
     */

    /**
     * 게시글 등록
     */

    /**
     * 게시글 수정
     */

    /**
     * 게시글 삭제
     */
}
