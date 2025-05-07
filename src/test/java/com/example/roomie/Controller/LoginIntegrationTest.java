package com.example.roomie.Controller;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtService;
import com.example.roomie.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    private final String testToken = "mock-token";

    @BeforeEach
    void setup() {
        User newUser = User.builder()
                .email("test@example.com")
                .nickname("testuser")
                .socialType(SocialType.Google)
                .socialToken("abc123")
                .role(Role.GUEST) // 최초 로그인
                .build();
        userRepository.save(newUser);
    }

    @Test
    void 최초_로그인_성공시_guest_accessToken_발급() throws Exception {
        when(jwtService.createAccessToken(any())).thenReturn("guest-access-token");

        mockMvc.perform(get("/oauth2/callback") // 실제 경로에 맞게 조정
                        .with(oauth2Login()
                                .attributes(attrs -> {
                                    attrs.put("email", "test@example.com");
                                    attrs.put("sub", "abc123");
                                })
                                .clientRegistration(clientRegistration("google"))
                        )
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("http://localhost:3000/oauth/callback?accessToken=*&role=GUEST"));
    }

    @Test
    void 이후_로그인시_정상_redirect_및_refreshToken_쿠키_설정() throws Exception {
        User user = userRepository.findByEmail("test@example.com").orElseThrow();
        user.updateRole(Role.USER);
        user.updateRefreshToken("existing-refresh-token");
        userRepository.save(user);

        when(jwtService.extractRefreshToken(any())).thenReturn(Optional.of("existing-refresh-token"));
        when(jwtService.isTokenValid(any())).thenReturn(true);
        when(jwtService.createAccessToken(any())).thenReturn("new-access-token");

        mockMvc.perform(get("/oauth2/callback") // 실제 로그인 완료 후 경로에 맞게 수정
                        .with(oauth2Login()
                                .attributes(attrs -> {
                                    attrs.put("email", "test@example.com");
                                    attrs.put("sub", "abc123");
                                })
                                .clientRegistration(clientRegistration("google"))
                        )
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Set-Cookie", containsString("refreshToken=")))
                .andExpect(redirectedUrlPattern("http://localhost:3000/oauth/callback?accessToken=*&role=USER"));
    }

    private ClientRegistration clientRegistration(String registrationId) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId("mock-client-id")
                .clientSecret("mock-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();
    }
}

