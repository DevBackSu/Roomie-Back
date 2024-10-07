package com.example.roomie.Controller;

import com.example.roomie.DTO.UserDto;
import com.example.roomie.SwaggerForm.MainControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MainController implements MainControllerDocs {

    @GetMapping("/api/data")
    public List<String> getData(){
        return Arrays.asList("Data1", "Data2","데이터3");
    }

    @GetMapping("/api/mypage")
    public List<String> getMyPage(){
        return Arrays.asList("hi", "start", "end", "go");
    }

    @GetMapping("/")
    public String getHomeDate() {
        return "home";
    }
}
