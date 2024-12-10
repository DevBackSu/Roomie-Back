package com.example.roomie.Controller;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.DTO.UserDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Service.UserService;
import com.example.roomie.SwaggerForm.UserControllerDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController implements UserControllerDocs {

    private UserService userService;

    // info DTO 만들어서 가입용 DTO 생성 -> 저장하기
    @PostMapping("/info")
    public ResponseEntity<String> saveUserInfo(@RequestBody UserSingUpDTO userSingUpDTO, @RequestHeader("Authorization") String authHeader) {
        log.info("saveUserInfo 접근");

        System.out.println("\n\n\n-----------------------\n");
        System.out.println("saveUserInfo accessToken : " + authHeader);
        System.out.println("\n-----------------------\n\n\n");



        return ResponseEntity.ok("success");
    }

}
