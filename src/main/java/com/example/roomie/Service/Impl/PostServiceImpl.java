package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.FileDTO;
import com.example.roomie.DTO.PostDTO;
import com.example.roomie.Entity.File;
import com.example.roomie.Entity.Post;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.FileRepository;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    private Long validateAccessToken(String authHeader) {
        String accessToken = jwtService.extractTokenAccessToken(authHeader);
        return jwtService.accessTokenToId(accessToken);
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

        if(postDTO != null) {
            List<FileDTO> files = fileRepository.findFileDTOsByPostCheckId(postCheckId);
            if(!files.isEmpty()) {
                postDTO.setFiles(files);
            }
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

        // 4. 저장
        postRepository.save(post);

        // 5. 첨부파일 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String originName = file.getOriginalFilename();
                        String fileName = UUID.randomUUID().toString();
                        String fileType = file.getContentType();

                        String filePath = "C:\\private\\file\\" + fileName + "." + fileType;
                        java.io.File dest = new java.io.File(filePath);
                        file.transferTo(dest); // IOException 발생 가능

                        File fileEntity = File.builder()
                                .post(post)
                                .filePath(filePath)
                                .originName(originName)
                                .fileName(fileName)
                                .fileType(fileType)
                                .build();

                        fileRepository.save(fileEntity);
                    } catch (IOException e) {
                        return -1L;
                    }
                }
            }
        }
        return post.getPostCheckId(); // 또는 post.getPostCheckId() 함께 리턴 가능
    }
}
