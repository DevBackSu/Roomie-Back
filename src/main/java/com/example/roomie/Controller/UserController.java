package com.example.roomie.Controller;

import com.example.roomie.DTO.UserOtherDTO;
import com.example.roomie.DTO.UserSingUpDTO;
import com.example.roomie.Service.UserService;
import com.example.roomie.SwaggerForm.UserControllerDocs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController implements UserControllerDocs {

    private final UserService userService;

    /**
     * 사용자 정보 호출 시 사용하는 api
     * @param authHeader : access token
     * @return response
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> findUser(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();

        try {

            // User service에서 findUser하는 부분 넣기

        } catch (Exception e) {
            log.error("Error while saving user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 사용자 정보 (mypage 정보) 최초 수정 시 호출
     * /info는 oauth 후 결과 반환을 위한 api여서 다른 곳에서 사용 시 오류가 발생함
     * @param userSingUpDTO 사용자 정보 입력 시 필요한 DTO
     * @param authHeader access token 값
     * @return response(success, message)
     */
    @PostMapping("/info")
    public ResponseEntity<Map<String, Object>> saveUserInfo(@RequestBody UserSingUpDTO userSingUpDTO, @RequestHeader("Authorization") String authHeader) {  // 쿠키값이 없으면 null이 반환됨
        log.info("saveUserInfo 접근");
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        try {

            response = userService.saveUserInfo(userSingUpDTO, authHeader);

            String success = response.get("success").toString();

            if(success.equals("true")) {
                ResponseCookie cookie = ResponseCookie.from("refreshToken", response.get("refreshToken").toString())
                        .maxAge(1209600000)
                        .path("/")
                        .secure(true)
                        .sameSite("None")
                        .httpOnly(true)
                        .build();

                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

                response.remove("refreshToken");

                // 회원 가입 성공
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(response);
            }
            else if(success.equals("false")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }


        } catch (Exception e) {
            //회원 가입 중 오류 발생
            log.error("Error while saving user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 사용자의 특성 / 자기소개 저장
     */
    @PostMapping("/infoOther")
    public ResponseEntity<Map<String, Object>> saveUserInfoOther(@RequestBody UserOtherDTO userOtherDTO, @RequestHeader("Authorization") String authHeader) {
        log.info("사용자의 특성 및 자기소개 저장을 위한 saveUserInfoOther 접근");
        Map<String, Object> response = new HashMap<>();
        try {

            userService.saveUserInfoOther(userOtherDTO, authHeader);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error while saving user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 사용자 정보 삭제
     * @param authHeader access token 값
     * @return response(success, message)
     */
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUserInfo(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        try {
            response = userService.deleteUser(authHeader);

            ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();

            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);

        } catch (Exception e) {
            log.error("Error while saving user info : " , e);
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
