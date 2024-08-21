package com.example.roomie.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {
    @GetMapping("/api/data")
    public List<String> getData(){
        return Arrays.asList("Data1", "Data2","데이터3");
    }

    @GetMapping("/")
    public String getHomeDate() {
        return "home";
    }
}
