package com.example.roomie.Service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MainService {
    Map<String, Object> getStatistics();
//    public Map<String, Object> getCrank();
    List<String> getLrank();
}
