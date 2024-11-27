package com.example.roomie.Controller;

import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok("로그인 페이지로 이동");
    }

//    @PostMapping("/google")
//    public ResponseEntity<?> googleLogin(@RequestBody)


}
