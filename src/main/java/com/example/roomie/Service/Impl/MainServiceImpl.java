package com.example.roomie.Service.Impl;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.Entity.User;
import com.example.roomie.Repository.MainRepository;
import com.example.roomie.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final MainRepository mainRepository;

    public Map<String, Object> getStatistics() {
        Map<String, Object> response = new HashMap<>();

        // mainAnimal만 조회
        List<Integer> mainAnimals = mainRepository.findMainAnimal();

        // 1과 2의 개수를 세기 위한 변수
        long count1 = mainAnimals.stream().filter(i -> i == 1).count();
        long count2 = mainAnimals.stream().filter(i -> i == 2).count();

        // 전체 개수
        long totalCount = mainAnimals.size();

        // 비율 계산
        if (totalCount > 0) {
            // 소수점 이하 버림 (내림 처리) 후 double로 계산
            double percentage1 = Math.floor((count1 * 100.0) / totalCount);
            double percentage2 = Math.floor((count2 * 100.0) / totalCount);

            // 퍼센트 값 저장
            response.put("1", String.valueOf((long) percentage1));  // long으로 변환하여 저장
            response.put("2", String.valueOf((long) percentage2));  // long으로 변환하여 저장
        } else {
            response.put("1", "0");
            response.put("2", "0");
        }

        return response;
    }

//    public Map<String, Object> getCrank() {
//        Map<String, Object> response = new HashMap<>();
//
//    // 1~5순위를 직접 조회하든 각각 조회해서 합치든 해야 할 듯
//
//        System.out.println("\n\n\n-------------------------------\n");
//        System.out.println(rank.toString());
//        System.out.println("\n-------------------------------\n\n\n");
//
//        response.put("rank", rank);
//        return response;
//    }

    public List<String> getLrank() {
        List<String> rank = mainRepository.findLrank();

        System.out.println("\n\n\n-------------------------------\n");
        for (String s : rank) {
            System.out.println(s);
        }
        System.out.println("\n-------------------------------\n\n\n");
        return rank;
    }
}
