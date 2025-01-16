package com.example.roomie.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MainService {
    public Map<String, Object> getStatistics();
    public Map<String, Object> getCrank();
}
