package com.example.roomie.Service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MainService {
    Map<String, Object> getStatistics();
    List<String> getCrank();
    List<String> getLrank();
}
