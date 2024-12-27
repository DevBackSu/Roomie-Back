package com.example.roomie.Controller;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.Service.MainService;
import com.example.roomie.SwaggerForm.MainControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")
public class MainController implements MainControllerDocs {
    private final MainService mainService;

    @GetMapping("/statistics") // 메인 화면 중 종달새/올빼미 통계값 반환
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            statistics = mainService.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            statistics.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(statistics);
        }
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
