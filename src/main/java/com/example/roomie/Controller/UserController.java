package com.example.roomie.Controller;

import com.example.roomie.DTO.RankDTO;
import com.example.roomie.DTO.UserDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Service.UserService;
import com.example.roomie.SwaggerForm.UserControllerDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController implements UserControllerDocs {

    private final UserService userService;

    // info DTO 만들어서 가입용 DTO 생성 -> 저장하기
    @PostMapping("/info")
    public ResponseEntity<Map<String, Object>> saveUserInfo(@RequestBody UserSingUpDTO userSingUpDTO, @RequestHeader("Authorization") String authHeader,
                                               @CookieValue(value = "refreshToken", required = false) String refreshToken) {  // 쿠키값이 없으면 null이 반환됨
        log.info("saveUserInfo 접근");

        System.out.println("\n\n\n-----------------------\n");
        System.out.println("saveUserInfo accessToken : " + authHeader);
        System.out.println("refreshToken : " + refreshToken);
        System.out.println("userSingUpDTO\n" + userSingUpDTO.toString());
        System.out.println("\n-----------------------\n\n\n");

        try {
            Map<String, Object> token = userService.saveUserInfo(userSingUpDTO, authHeader, (refreshToken != null ? refreshToken : ""));

            // 회원 가입 성공
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            //회원 가입 중 오류 발생
            log.error("Error while saving user info : " , e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
