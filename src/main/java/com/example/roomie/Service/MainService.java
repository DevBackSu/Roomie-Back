package com.example.roomie.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MainService {
    public ResponseEntity<Map<String, Object>> getStatistics();
}
