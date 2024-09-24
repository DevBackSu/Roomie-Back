package com.example.roomie.Config;

import com.example.roomie.JWT.JwtServiceImpl;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // JWT filter를 적용하지 않는 URL

    private final JwtServiceImpl jwtService; // jwt
    private final UserRepository userRepository; // 사용자 jpa

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL))
        {
            filterChain.doFilter(request, response); // login 요청이 들어오면 다음 필터 호출하게 함 -> 아래 return 줄로 이동
            return; // return을 통해 현재 필터 진행을 막기 (없으면 필터를 계속 진행함) => 즉, login 요청이 들어오면 필터를 진행하지 않고 종료하겠다는 뜻.
        }
    }
}
