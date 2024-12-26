package com.example.roomie.Controller;

import com.example.roomie.DTO.UserPageDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Service.MyPageService;
import com.example.roomie.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mypage")
@Slf4j
public class MypageController {

    private final MyPageService myService;

    /**
     * 사용자가 MyPage를 호출했을 경우 실행됨
     * @param authHeader : access token 값
     * @return response(success, 실패 시 message or 성공 시 userdata)
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        log.info("my page 접근");
        Map<String, Object> response = new HashMap<>();

        try {
            response = myService.getUserInfo(authHeader);

            String success = response.get("success").toString();

            if(success.equals("true")) {
                return ResponseEntity.ok(response);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error while getting user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 마이 페이지 수정을 위한 API
     * @param authHeader : access token
     * @param userPageDTO : 사용자가 입력한 값
     * @return response
     */
    @PostMapping("/mypageUpdage")
    public ResponseEntity<Map<String, Object>> updateUserInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UserPageDTO userPageDTO) {
        log.info("updage User Info 접근");
        Map<String, Object> response = new HashMap<>();

        try {

            response = myService.saveUserInfo(userPageDTO, authHeader);

            // 회원 가입 성공
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            //회원 가입 중 오류 발생
            log.error("Error while saving user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
