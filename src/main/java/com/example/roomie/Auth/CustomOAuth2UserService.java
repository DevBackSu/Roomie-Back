package com.example.roomie.Auth;

import com.example.roomie.Entity.User;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 어떤 소셜 로그인인지를 구분하기 위한 값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();  // OAuth 로그인 시 키가 되는 값

        OAuthAttribute attribute = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());  // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담음

        User user = saveOrUpdate(attribute);

        // 세션에 사용자 정보 저장
//        httpSession.setAttribute("user", new SessionUser(user));  // http session과 user의 객체 수가 달라서 의존성 주입을 직접 해야 함

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                    attribute.getAttributes(),
                    attribute.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttribute attribute) {
        User user = userRepository.findByEmail(attribute.getEmail())  // email을 기준으로 사용자 정보를 DB에서 조회함
                .map(entity -> entity.update(attribute.getName()))    // 사용자가 이메일 기준으로 존재하면 해당 사용자의 정보 update
                .orElse(attribute.toEntity());                        // 사용자가 이메일 기준으로 존재하지 않으면 새 User 객체 생성 -> toEntity를 통해 Role == GUEST

        return userRepository.save(user);
    }
}
