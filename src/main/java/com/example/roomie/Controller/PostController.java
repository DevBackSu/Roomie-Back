package com.example.roomie.Controller;

import com.example.roomie.DTO.PostDTO;
import com.example.roomie.Entity.Post;
import com.example.roomie.Service.PostService;
import com.example.roomie.Service.UserService;
import com.example.roomie.SwaggerForm.PostControllerDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posting")
@Slf4j
public class PostController implements PostControllerDocs {
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
    @GetMapping("/detail/{postCheckId}")
    public ResponseEntity<Map<String, Object>> getPostDetail(@PathVariable Long postCheckId, @RequestHeader("Authorization") String authHeader) {
        log.info("post detail 접근");
        Map<String, Object> response = new HashMap<>();
        try {
            PostDTO postDetail = postService.getPostDetail(postCheckId, authHeader);

            if(postDetail == null) {
                response.put("success", "false");
                response.put("message", "존재하지 않는 게시글 입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            else if(postDetail.getPostId() == -1L) {
                response.put("success", "false");
                response.put("message", "유효하지 않은 사용자 입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            response.put("success", true);
            response.put("postDetail", postDetail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 게시글 등록
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestPart("post") PostDTO requestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestHeader("Authorization") String authHeader) {

        log.info("게시글 등록 요청: {}", requestDTO.getTitle());
        Map<String, Object> response = new HashMap<>();

        try {
            Long postCheckId = postService.createPostWithFiles(requestDTO, files, authHeader);

            if(postCheckId == null) {
                response.put("success", "false");
                response.put("error", "사용자 인증에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            else if(postCheckId == -1L) {
                response.put("success", "false");
                response.put("error", "파일 저장에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            response.put("success", true);
            response.put("postCheckId", postCheckId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("게시글 등록 중 오류 발생", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 게시글 수정
     */

    /**
     * 게시글 삭제
     */
}
