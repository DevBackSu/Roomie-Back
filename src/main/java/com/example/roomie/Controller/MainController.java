package com.example.roomie.Controller;

import com.example.roomie.SwaggerForm.MainControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")
public class MainController implements MainControllerDocs {

    @GetMapping("/test")
    public List<String> getData(){
        return Arrays.asList("Data1", "Data2","데이터3");
    }

    @GetMapping("/mypage")
    public List<String> getMyPage(){
        return Arrays.asList("hi", "start", "end", "go");
    }

    @GetMapping("/home") // 이게 원래 /
    public String getHomeDate() {
        return "home";
    }
}
