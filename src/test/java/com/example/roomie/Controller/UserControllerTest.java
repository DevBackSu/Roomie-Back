package com.example.roomie.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserControllerTest {
    @Test
    public void 사용자_리스트_반환(){
        List<String> data = new ArrayList<>();
        data.add("첫번째");
        data.add("두번째");

        System.out.println("\n\n\n-----------------------\n");
        System.out.println(data);
        System.out.println("\n-----------------------\n\n\n");
    }

}