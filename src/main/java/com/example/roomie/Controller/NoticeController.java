package com.example.roomie.Controller;

import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getNoticeList() {
        log.info("getNoticeList 접근");
        Map<String, Object> response = new HashMap<>();

        try {
            response = noticeService.getNoticeList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.put("success", "false");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
