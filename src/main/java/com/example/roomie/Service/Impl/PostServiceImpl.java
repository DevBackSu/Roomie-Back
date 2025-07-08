package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.PostDTO;
import com.example.roomie.Entity.Post;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.PostRepository;
import com.example.roomie.Repository.UserRepository;
import com.example.roomie.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final String INVALID_ACCESS_TOKEN_MSG = "Invalid access token";
    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    private Long validateAccessToken(String authHeader) {
        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        return jwtService.accessTokenToId(accessToken);
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createSuccessResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        return response;
    }

    private Long generateRandomPostCheckId() {
        Long randomId;
        do {
            randomId = (long) (Math.random() * 900000) + 100000;
        } while (postRepository.existsByPostCheckId(randomId));
        return randomId;
    }

    public Map<String, Object> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postId").descending());

        Page<Post> postPage = postRepository.findAll(pageable); // Page 객체로 받아야 totalElements 사용 가능

        Map<String, Object> result = new HashMap<>();
        result.put("postList", postPage.getContent()); // 현재 페이지에 해당하는 게시글 리스트
        result.put("totalPosts", postPage.getTotalElements()); // 전체 게시글 수

        return result;
    }

    public PostDTO getPostDetail(Long postCheckId, String token) {
       Long userId = validateAccessToken(token);
       if(userId == -1) {
           PostDTO postDTO = new PostDTO();
           postDTO.setPostId(-1L);
           return postDTO;
       }

       PostDTO postDTO = postRepository.findPostDetailByCheckId(postCheckId);

        if(postDTO == null) {
            return null;
        }

        return postDTO;
    }

    // 여기 정리하기!!
    public Long createPostWithFiles(PostDTO requestDTO, List<MultipartFile> files, String authHeader) {
        // 1. 사용자 인증
        Long userId = validateAccessToken(authHeader);
        if (userId == -1) return null;

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            return null;
        }

        // 2. postCheckId 랜덤 생성
        Long randomPostCheckId = generateRandomPostCheckId();

        // 3. 게시글 생성
        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .writeDtm(LocalDateTime.now().toString())
                .user(user.get()) // user는 Optional이기 때문에 get으로 꺼내줘야 함
                .postCheckId(randomPostCheckId)
                .build();

        // 4. 첨부파일 처리
//        if (files != null && !files.isEmpty()) {
//            for (MultipartFile file : files) {
//                String savedPath = fileUploadUtil.upload(file);
//
//                FileEntity fileEntity = FileEntity.builder()
//                        .filePath(savedPath)
//                        .originalName(file.getOriginalFilename())
//                        .post(post) // 연관관계 설정
//                        .build();
//
//                post.addFile(fileEntity); // 양방향 관계 설정
//            }
//        }

        // 5. 저장
        postRepository.save(post);
        return post.getPostCheckId(); // 또는 post.getPostCheckId() 함께 리턴 가능
    }
}
