package com.example.roomie.Service;

import com.example.roomie.Entity.Post;

import java.util.List;
import java.util.Map;

public interface PostService {

    Map<String, Object> getPostList(int page, int size);

    Map<String, Object> getPostDetail(Long postCheckId, String token);
}
