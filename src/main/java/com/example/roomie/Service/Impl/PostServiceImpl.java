package com.example.roomie.Service.Impl;

import com.example.roomie.Entity.Post;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.PostRepository;
import com.example.roomie.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;

    public Map<String, Object> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postId").descending());

        Page<Post> postPage = postRepository.findAll(pageable); // Page 객체로 받아야 totalElements 사용 가능

        Map<String, Object> result = new HashMap<>();
        result.put("postList", postPage.getContent()); // 현재 페이지에 해당하는 게시글 리스트
        result.put("totalPosts", postPage.getTotalElements()); // 전체 게시글 수

        return result;
    }
}
