package com.example.roomie.Controller;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.DTO.UserDTO;
import com.example.roomie.SwaggerForm.MainControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")
public class MainController implements MainControllerDocs {

    @GetMapping("/user")
    public UserDTO getUser() {
        UserDTO data = new UserDTO();
        return data;
    }

    @GetMapping("/other") // 친구 관계의 사용자 3명 정도를 반환
    public Map<String, UserDTO> getOther() {
        Map<String, UserDTO> other = new HashMap<>();
        return other;
    }

    @GetMapping("/statistics") // 메인 화면 중 종달새/올빼미 통계값 반환
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("새", 2); // 종달새 count 값
        statistics.put("부엉이", 2); // 올빼미 count 값
        return statistics;
    }

    @GetMapping("/Crank") // 메인 화면 중 특징 순위 반환
    public Map<String, RankDTO> getCrank() {
        Map<String, RankDTO> crank = new HashMap<>();
        return crank;
    }

    @GetMapping("/Lrank") // 메인 화면 중 지역 순위 반환
    public Map<String, RankDTO> getLrank() {
        Map<String, RankDTO> Lrank = new HashMap<>();
        return Lrank;
    }
}
