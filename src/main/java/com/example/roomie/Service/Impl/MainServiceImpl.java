package com.example.roomie.Service.Impl;

import com.example.roomie.Repository.MainRepository;
import com.example.roomie.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MainRepository mainRepository;

    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> response = new HashMap<>();
        List<Integer> main = mainRepository.findByMainAnimal();


        return ResponseEntity.ok(response);
    }
}
