package com.example.roomie.Auth;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * 로그인 성공 시 jwt token 생성 -> refresh token에 따라 재로그인 vs 로그인 유지하기)
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            System.out.println("\n\n\n--------------------------\n");
            System.out.println("login success handler");
            System.out.println(oAuth2User.getEmail());
            System.out.println(oAuth2User.getName());
            System.out.println(oAuth2User.getAttributes());
            System.out.println(oAuth2User.getSocialToken());
            System.out.println(oAuth2User.getRole());
            System.out.println("\n--------------------------\n\n\n");
//            Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
            Optional<User> user = userRepository.findBySocialToken(oAuth2User.getSocialToken());

            if (user.isEmpty()) {
                log.info("일치하는 사용자가 없습니다.");
                response.sendRedirect("http://localhost:3000/error");
                return;
            }

            User u = user.get();
//            oAuth2User.setRole(u.getRole());

            // 사용자의 Role이 Guest면 처음 요청한 사용자이기 때문에 회원가입 페이지로 리다이렉트 필요
            if (oAuth2User.getRole() == Role.GUEST) {
                handleGuestLogin(response, u);
            } else {
                handleLogin(request, response, u);
            }
        } catch (Exception e) {
            log.info("LoginSuccessHandler에서 오류 발생");
            throw e;
        }

    }


    private void handleGuestLogin(HttpServletResponse response, User u) throws IOException {

        System.out.println("\n\n\n-----------------------\n");
        System.out.println(u.toString());
        System.out.println("\n-----------------------\n\n\n");

        String accessToken = jwtService.createAccessToken(u.getId());

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:3000/oauth/callback")
                .queryParam("accessToken", URLEncoder.encode(accessToken, StandardCharsets.UTF_8))
                .queryParam("role", u.getRole().toString())
                .toUriString();
        response.sendRedirect(url);

        log.info("Redirect URL : " + url);
        log.info("Guest 사용자 로그인 처리 완료");
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, User u) throws IOException {
        Long user_id = u.getId();
        String accessToken;
        String refreshToken = null;

        // 헤더에서 리프레시 토큰 추출
        Optional<String> opToken = jwtService.extractRefreshToken(request);

        // 리프레시 토큰이 있을 경우
        if (opToken.isPresent()) {
            refreshToken = opToken.get();

            // 리프레시 토큰의 유효기간이 지나지 않았을 경우 -> 액세스 토큰만 생성
            if (jwtService.isTokenValid(refreshToken)) {
                accessToken = jwtService.createAccessToken(user_id);
            }
            // 리프레시 토큰의 유효 기간이 지났을 경우 -> 두 토큰 모두 새로 생성
            else {
                refreshToken = jwtService.createRefreshToken();
                accessToken = jwtService.createAccessToken(user_id);
                jwtService.updateRefreshToken(user_id, refreshToken);
            }
        }
        // 리프레시 토큰이 없으면 새로 생성
        else {
            refreshToken = jwtService.createRefreshToken();
            accessToken = jwtService.createAccessToken(user_id);
            jwtService.updateRefreshToken(user_id, refreshToken);
        }
//        // 쿠키에 refreshToken 설정
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 금지
////        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송 (개발 환경에서는 false로 설정 가능)
//        refreshTokenCookie.setSecure(false); // HTTPS에서만 전송 (개발 환경에서는 false로 설정 가능)
//        refreshTokenCookie.setPath("/"); // 쿠키가 모든 경로에서 유효
//        refreshTokenCookie.setMaxAge(14 * 24 * 60 * 60); // 쿠키 만료 기간: 14일
//
//        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가

        // `Set-Cookie` 헤더에 refreshToken 설정
//        String cookie = String.format(
//                "refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; Secure",
//                refreshToken,
//                14 * 24 * 60 * 60 // 14일
//        );
//        response.addHeader("Set-Cookie", cookie);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(1209600000)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        // 액세스 토큰을 URL에 포함하여 프론트엔드로 전달
        String url = String.format(
                "http://localhost:3000/oauth/callback?accessToken=%s&role=%s",
                URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
                URLEncoder.encode(u.getRole().name(), StandardCharsets.UTF_8)
        );

        response.sendRedirect(url);
        log.info("Login 처리 완료: AccessToken과 RefreshToken 전달");
    }
}
