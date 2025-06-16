package com.example.roomie.Controller;

import com.example.roomie.Auth.*;
import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.Instant;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LoginIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    private OAuth2UserRequest userRequest;
    private OAuth2UserInfo oAuth2User;
    private User testUser;

    @BeforeEach
    void setUp() {
        // OAuth2 로그인 사용자 정보 설정
        Map<String, Object> attributes = Map.of(
                "sub", "123456789",
                "name", "Test User",
                "email", "test@example.com"
        );
        oAuth2User = new GoogleOAuth2UserInfo(attributes);

        // 테스트용 사용자 설정
        testUser = User.builder()
                .email("test@example.com")
                .nickname("Test User")
                .socialType(SocialType.Google)
                .socialToken("123456789")
                .role(Role.USER)
                .build();

        // ClientRegistration 및 OAuth2AccessToken 설정
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("google")
                .tokenUri("https://oauth2.googleapis.com/token")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .build();

        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "mock-access-token",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        userRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
    }


    @Test
    @DisplayName("소셜 로그인 성공 테스트 - Google")
    void 소셜로그인_성공() {
        when(userRepository.findBySocialToken(any())).thenReturn(Optional.of(testUser));

        OAuth2User authenticatedUser = customOAuth2UserService.loadUser(userRequest);

        assertNotNull(authenticatedUser);
        assertEquals("닉네임이 일치하지 않음", "Test User", authenticatedUser.getName());
    }


    @Test
    @DisplayName("최초 로그인 시 GUEST 역할 부여 테스트")
    void 최초_로그인_시_GUEST_설정() {
        when(userRepository.findBySocialToken(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(testUser);

        OAuth2User authenticatedUser = customOAuth2UserService.loadUser(userRequest);

        verify(userRepository, times(1)).save(any());
        assertEquals("이메일이 일치하지 않음", "test@example.com", ((CustomOAuth2User) authenticatedUser).getEmail());
    }

    @Test
    @DisplayName("JWT 토큰 발급 테스트")
    void JWT_토큰_발급() {
        when(jwtService.createAccessToken(any())).thenReturn("mockAccessToken");
        when(jwtService.createRefreshToken()).thenReturn("mockRefreshToken");

        String accessToken = jwtService.createAccessToken(testUser.getId());
        String refreshToken = jwtService.createRefreshToken();

        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        assertEquals("Access Token이 일치하지 않음", "mockAccessToken", accessToken);
        assertEquals("Refresh Token이 일치하지 않음", "mockRefreshToken", refreshToken);
    }
}
