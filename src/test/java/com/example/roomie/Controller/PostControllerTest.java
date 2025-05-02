package com.example.roomie.Controller;

import com.example.roomie.Entity.Post;
import com.example.roomie.Repository.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

//    @BeforeEach
//    void setUp() {
//        List<Post> posts = IntStream.range(1, 25).mapToObj(i ->
//                Post.builder()
//                        .title("테스트 제목" + i)
//                        .content("테스트 내용" + i)
//                        .userId(1L)
//                        .writeDtm(LocalDateTime.now().minusDays(i).toString())
//                        .build()
//        ).toList();
//        postRepository.saveAll(posts);
//    }

    /**
     * /api/posting은 token 인증 부분이 없음 -> API 테스트 진행
     * @throws Exception
     */
    @Test
    public void 게시글_목록_조회_성공() throws Exception {
        mockMvc.perform(get("/api/posting")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postData.postList").isArray())
                .andExpect(jsonPath("$.postData.postList", hasSize(lessThanOrEqualTo(10))))
                .andExpect(jsonPath("$.postData.totalPosts").isNumber());
    }

    @Test
    public void 게시글_빈페이지_조회() throws Exception {
        mockMvc.perform(get("/api/posting")
                        .param("page", "10") // 0-based index
                        .param("size", "10"))
                .andExpect(status().isOk()) // 위 api 요청의 status가 200인지 확인
                .andExpect(jsonPath("$.postData.postList").isArray()) //
                .andExpect(jsonPath("$.postData.postList", hasSize(lessThanOrEqualTo(0))))
                .andExpect(jsonPath("$.postData.totalPosts").isNumber());
    }

    @Test
    public void 게시글_잘못된_파라미터() throws Exception {
        mockMvc.perform(get("/api/posting")
                        .param("page", "-1")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }
}
