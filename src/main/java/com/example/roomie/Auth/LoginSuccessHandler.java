package com.example.roomie.Auth;

import com.example.roomie.DTO.UserDTO;
import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * 로그인 성공 시 jwt token 생성 -> refresh token에 따라 재로그인 vs 로그인 유지하기)
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

            // 사용자의 Role이 Guest면 처음 요청한 사용자이기 때문에 회원가입 페이지로 리다이렉트 필요
            if(oAuth2User.getRole() == Role.GUEST) {
                Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
                String accessToken = jwtService.createAccessToken(user.get().getId()); // 나는 토큰에 id만 넣을 예정 (email 넣을까도 생각했는데 굳이? 싶음. 최대한 사용자 정보를 안 담고 싶다)
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken); // 헤더에 key : Auth / value : Bearer ~~~ 가 추가됨
                response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 form으로 리다이렉트

                jwtService.sendAllToken(response, accessToken, null); // accessToken과 refreshToken을 헤더에 담아 회원가입 추가 정보 입력 폼으로 리다이렉트 함
                // 위 값을 보내면 프론트에서 회원가입 추가 정보 입력 폼으로 이동하도록 구현하기
            }
            else {
                // 한 번 이상 OAuth2 로그인을 한 사용자일 경우, 추가 정보를 이미 기입했기 때문에 token만 발급해서 헤더에 담음 -> 이동 필요 X
                loginSuccess(request, response, oAuth2User); // 로그인에 성공한 경우 access와 refresh 토큰 생성
            }
        }
        catch (Exception e) {
            throw e;
        }

    }


    /**
     * 소셜 로그인 시에도 무조건 토큰을 생성하지 말고 jwt 인증 필터처럼 refreshToken 유/무에 따라서 다르게 처리하기!
     * - refresh가 있고 만료 전인 경우
     *      - access가 있는지 확인하고 access가 만료되었으면 재발급 / 아니면 둘 다 만료 전이기 때문에 상관 X
     * - refresh가 없거나 만료된 경우
     *      - 재로그인
     * @param response
     * @param oAuth2User
     * @throws IOException
     */
    private void loginSuccess(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());
        Long userId = user.get().getId();

        if(user.isEmpty()) {
            log.info("일치하는 사용자가 없습니다.");
            throw new IOException("일치하는 사용자가 없습니다.");
        }

//         refresh token이 있는지 확인하기 위해 Request 헤더에서 추출해 검사
        Optional<String> opToken = jwtService.extractRefreshToken(request);

        // 추출한 opToken에 값이 존재하는지 확인
        if(opToken.isPresent()) {
            // 값이 존재할 경우 아래 진행
            String refreshToken = opToken.get();

            // 유효 여부 확인 -> refresh token이 유효하면 access token 재발급
            if(jwtService.isTokenValid(refreshToken)) {
                String accessToken = jwtService.createAccessToken(userId);
                jwtService.sendAccessToken(response, accessToken);
            }
            // 유효하지 않다면 -> 둘 다 재발급
            else {
                String newRefreshToken = jwtService.createRefreshToken();
                String accessToken = jwtService.createAccessToken(userId);

                jwtService.updateRefreshToken(userId, newRefreshToken);

                jwtService.sendAllToken(response, accessToken, newRefreshToken);
            }
        }
        // 값이 존재하지 않을 경우
        else {
            // 다른 무언가가 필요한가?
        }
    }
}
