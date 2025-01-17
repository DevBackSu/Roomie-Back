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
import java.util.List;
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

    // 구현한 DB 상 특성은 조인으로 엮여 있음. -> crank는 service쪽을 다시 작성해야 할 것 같다.
//    @GetMapping("/crank") // 메인 화면 중 특징 순위 반환
//    public  ResponseEntity<Map<String, Object>> getCrank() {
//        Map<String, Object> crank = new HashMap<>();
//        try {
//            crank = mainService.getCrank();
//            return ResponseEntity.ok(crank);
//        } catch (Exception e) {
//            crank.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(crank);
//        }
//    }

    @GetMapping("/lrank") // 메인 화면 중 지역 순위 반환
    public  ResponseEntity<Map<String, Object>> getLrank() {
        Map<String, Object> Lrank = new HashMap<>();
        try {
            List<String> localRank = mainService.getLrank();

            System.out.println("\n\n\n-------------------------------\n");
            for (String s : localRank) {
                System.out.println(s);
            }
            System.out.println("\n-------------------------------\n\n\n");

            if(localRank.isEmpty()) {
                localRank.add("아직 순위가 존재하지 않아요");
            }
            Lrank.put("rank", localRank);
            // 만약 Lrank가 null일 때는 다른 return주기
            return ResponseEntity.ok(Lrank);
        } catch (Exception e) {
            Lrank.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Lrank);
        }
    }
}
