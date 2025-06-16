package com.example.roomie.Controller;

import com.example.roomie.Entity.Post;
import com.example.roomie.Service.PostService;
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
@RequestMapping("/api/posting")
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;

    /**
     * 전체 게시글 목록 조회
     * JPA의 Pageable을 사용해 페이지네이션 함
     * 여기에 경로로 / 을 추가하면 프론트에서 호출할 때 /api/posting/?.. 이런 식으로 호출해야 함
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPostList(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        log.info("getPostList 접근");
        Map<String, Object> response = new HashMap<>();

        if (page < 0 || size <= 0) {
            response.put("success", "false");
            response.put("error", "올바르지 않은 page 또는 size 값입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            Map<String, Object> postData = postService.getPostList(page, size);

            response.put("success", "true");
            response.put("postData", postData);

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
    @GetMapping("/detail/{postChekId}")
    public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable Long postChekId) {
        log.info("post detail 접근");
        Map<String, Object> response = new HashMap<>();
        try {
            Post post = postService.getPostDetail(postChekId);
            response.put("success", "true");
            response.put("postData", post);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

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
