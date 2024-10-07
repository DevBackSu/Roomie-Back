package com.example.roomie.Controller;

import com.example.roomie.DTO.UserDto;
import com.example.roomie.SwaggerForm.UserControllerDocs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class UserController implements UserControllerDocs {

    @GetMapping("/main/user")
    public UserDto getUser() {
        UserDto data = new UserDto();
        return data;
    }

    @GetMapping("main/other") // 친구 관계의 사용자 3명 정도를 반환
    public Map<String, UserDto> getOther() {
        Map<String, UserDto> other = new HashMap<>();
        return other;
    }

    @GetMapping("main/statistics") // 메인 화면 중 새/부엉이 통계값 반환
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("새", 2); // 새 count 값
        statistics.put("부엉이", 2); // 부엉이 count 값
        return statistics;
    }

    @GetMapping("main/Crank") // 메인 화면 중 특징 순위 반환
    public Map<String, RankDTO> getCrank() {
        Map<String, RankDto> crank = new HashMap<>();
        return crank;
    }

    @GetMapping("main/Lrank") // 메인 화면 중 지역 순위 반환
    public Map<String, RankDTO> getLrank() {
        Map<String, RankDto> Lrank = new HashMap<>();
        return Lrank;
    }


}
