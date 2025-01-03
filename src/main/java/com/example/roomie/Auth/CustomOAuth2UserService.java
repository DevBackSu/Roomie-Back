package com.example.roomie.Auth;

import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import com.example.roomie.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * OAuth2의 로그인 로직을 담당하는 Service 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    @Override  //OAuth2UserService 인터페이스의 loadUser 메서드를 재정의함
    /**
     * OAuth2 인증 과정에서 사용자 정보를 가져오기 위해 사용하는 메서드
     * OAuth2UserRequest : OAuth2 인증을 통해 받은 액세스 토큰, 클라이언트 정보, 인증 프로바이더 정보를 포함함
     * OAuth2User : 반환값. 구글, 카카오 등의 OAuth2 프로바이더에서 가져온 사용자 정보를 담고 있음
     */
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2Service의 loadUser() 실행 - OAuth2 로그인 요청 진입");
        /**
         * DefaultOAuth2UserService 객체를 생성 -> loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성한 후 반환하는 메서드.
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서 사용자의 정보를 얻은 후
         * 얻은 정보로 DefaultOAuth2User 객체를 생성해 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 사용자 정보를 담고 있게 된다.
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService(); // delegate에 new DefaultOAuth2UserService 생성. 이는 스프링 시큐리티에서 기본 제공하는 OAuth2UserService 구현체로, 주어진 OAuth2UserRequest를 이용해 인증 서버로부터 사용자 정보를 가져와 delegate에 저장함
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth2UserRequest를 받아 해당 OAuth2 프로바이더로부터 사용자 정보를 가져오는 메서드.
        // 반환된 oAuth2User 객체는 OAuth2 인증이 완료된 후 구글, 카카오 등의 프로바이더에서 제공하는 사용자 정보를 포함함. 예를 들어, Google 로그인을 시도했다면 구글에서 제공하는 name, email 등을 가지고 있음

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //OAuth2 로그인 시 키가 되는 값
        Map<String, Object> attribute = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값

        OAuthAttribute extractAttributes = OAuthAttribute.of(socialType, userNameAttributeName, attribute);

        User createdUser = getUser(extractAttributes, socialType); // getUser 메소드로 User 객체 생성 후 반환

        System.out.println("\n\n\n--------------------------\n");
        System.out.println("CustomOAuth2USerService");
        System.out.println(createdUser.toString());
        System.out.println("\n--------------------------\n\n\n");

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attribute,
                extractAttributes.getNameKey(),
                createdUser.getEmail(),
                createdUser.getNickname(),
                createdUser.getRole(),
                createdUser.getSocialToken()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialType.Naver;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialType.Kakao;
        }
        return SocialType.Google;
    }

    /**
     * 소셜 타입과 attributes에 들어 있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있으면 그대로 반환하고 없으면 saveUser() 호출 -> 회원 저장
     */
    private User getUser(OAuthAttribute attribute, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialToken(socialType, attribute.getOAuth2UserInfo().getToken()).orElse(null);

        if(findUser == null) {
            return saveUser(attribute, socialType);
        }

        return findUser;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장함 (이때, 사용자 정보는 socialType / socialToken / email / role만 있음)
     */
    private User saveUser(OAuthAttribute attribute, SocialType socialType) {
        User createdUser = attribute.toEntity(socialType, attribute.getOAuth2UserInfo());

        return userRepository.save(createdUser);
    }
}
