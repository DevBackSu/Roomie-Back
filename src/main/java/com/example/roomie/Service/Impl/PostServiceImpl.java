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

    public Boolean getEqualUser(Long postId, String authHeader) {
        Long userId = validateAccessToken(authHeader);
        System.out.println("\n\n\n-------------------------------\n");
        System.out.println(userId);
        System.out.println("\n-------------------------------\n\n\n");

        Boolean equalUser = postRepository.existsByPostIdAndUser_Id(postId, userId);

        System.out.println("\n\n\n-------------------------------\n");
        System.out.println(equalUser);
        System.out.println("\n-------------------------------\n\n\n");

        return equalUser;
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

    // 여기 정리하기!! -> 왜 파일 첨부 자체가 되지 않는가.. 저번에 테스트할 때는 됐던 것 같은데???
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

        System.out.println("\n\n\n-------------------------------\n");
        System.out.println(post);
        System.out.println("\n-------------------------------\n\n\n");

        // 4. 저장
        postRepository.save(post);

        System.out.println("\n\n\n-------------------------------\n");
        System.out.println("게시글 저장 완료 / 파일 저장 진행");
        System.out.println("\n-------------------------------\n\n\n");

        // 5. 첨부파일 저장
        if (files != null && !files.isEmpty()) {
            System.out.println("\n\n\n-------------------------------\n");
            System.out.println(files);
            System.out.println("\n-------------------------------\n\n\n");
            for (MultipartFile file : files) {
                System.out.println("파일 이름: " + file.getOriginalFilename());
                System.out.println("파일 크기: " + file.getSize());
                System.out.println("파일 isEmpty(): " + file.isEmpty());
                System.out.println("파일 content type : " + file.getContentType());
                if (!file.isEmpty()) {
                    try {
                        String originName = file.getOriginalFilename();
                        String fileName = UUID.randomUUID().toString();
                        String fileType = file.getContentType();
                        String extension = originName.substring(originName.lastIndexOf(".") + 1);

                        System.out.println("\n\n\n-------------------------------\n");
                        System.out.println("file type : " + fileType);
                        System.out.println("\n-------------------------------\n\n\n");
                        String filePath = "C:\\private\\file\\" + fileName + "." + extension;
                        java.io.File dest = new java.io.File(filePath);
                        file.transferTo(dest); // IOException 발생 가능 -> 여기가 문젠가?

                        File fileEntity = File.builder()
                                .post(post)
                                .filePath(filePath)
                                .originName(originName)
                                .fileName(fileName)
                                .fileType(fileType)
                                .build();

                        System.out.println("\n\n\n-------------------------------\n");
                        System.out.println(fileEntity);
                        System.out.println("\n-------------------------------\n\n\n");
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
