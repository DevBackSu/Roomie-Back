package com.example.roomie.Service;

import com.example.roomie.DTO.PostDTO;
import com.example.roomie.Entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {

    Map<String, Object> getPostList(int page, int size);

    PostDTO getPostDetail(Long postCheckId, String token);

    Long createPostWithFiles(PostDTO requestDTO, List<MultipartFile> files, String authHeader);

    Boolean getEqualUser(Long postId, String authHeader);
}
