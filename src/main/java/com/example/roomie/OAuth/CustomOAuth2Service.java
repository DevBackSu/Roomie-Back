package com.example.roomie.OAuth;

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
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private final UserRepository userRepository;

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

        /**
         * userRequest에서 registrationID를 추출한 후 registrationId로 socialType을 저장한다.
         * registrationId에는 공급자의 등록ID가 저장됨 (만약 사용자가 Google로 로그인을 했다면 여기에는 "google"이 저장되는 것
         * userNameAttributeName에는 사용자의 고유 ID 속성값을 저장함. (만약 사용자가 Google로 로그인을 했다면 여기에는 "sub"이 저장됨)
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 사용자가 로그인 한 OAuth2 공급자의 등록ID를 가져온다. 즉, 로그인에 사용된 OAuth2 제공자가 무엇인지 알려줌
        // userRequest.getClientRegistration() 메서드로 어떤 클라(OAuth 공급자)로부터 요청이 들어왔는지 나타내는 정보를 반환하고, getRegistrationId() 메서드로 OAuth2 공급자의 ID를 get함
        SocialType socialType = getSocialType(registrationId); // 등록ID의 값을 소셜타입 enum 클래스에 저장함 -> DB의 사용자 소셜타입 컬럼에 저장하기 위함
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 key가 되는 값
        // userRequest.getClientRegistration()으로 OAuth2 클라의 설정 정보를 가져온다. 그리고 getProviderDetatils().getUserInfoEndpoint()로 사용자의 프로필을 제공하는 엔드포인트를 가져온다. 마지막으로 getUserNameAttributeName()으로 OAuth2 제공자가 반환하는 사용자 정보 중 사용자의 고유 ID로 사용되는 속성의 이름을 가져온다.
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 사용자 정보(ex) 이메일, 이름, 프로필이미지 등)를 json 형태로 저장

        /**
         * 위에서 가져온 정보를 바탕으로 OAuthDTO 객체를 생성하고 그 정보를 이용해 사용자를 생성한 후 최종적으로 CustomOAuth2User 객체를 반환하는 과정이다.
         */
        OAuthDTO extractAttributes = OAuthDTO.of(socialType, userNameAttributeName, attributes); // 세 값을 바탕으로 적절한 OAuthDTO 객체를 생성
        // 사용자가 Google로 로그인했을 경우, socialType에는 GOOGLE / userNameAttributeName에는 sub / attributes에는 사용자의 Google 프로필 정보가 담긴다. 그리고 이 정보를 이용해 OAuthDTO 객체가 만들어진다.

        User createUser = getUser(extractAttributes, socialType); // 두 값을 이용해 새로운 사용자 객체를 생성한다.
        // getUser()로 속성과 소셜 타입에 해당하는 사용자를 DB에서 찾거나 생성하도록 한다.
        // 만약 사용자가 처음 로그인하면 이 메서드를 사용해 새 사용자를 DB에 생성한다.

        return new CustomOAuth2User(  // CustomOAuth2User 객체를 생성 및 반환한다.
                Collections.singleton(new SimpleGrantedAuthority(createUser.getRole().getKey())), // 사용자의 권한 정보를 나타냄. USER / ADMIN / GUEST
                attributes,  // OAuth2 제공자가 반환한 사용자 정보값
                extractAttributes.getNameKey(),  // 식별키 (Google은 "sub" 등)
                createUser.getEmail(), // 사용자의 이메일
                createUser.getRole()  // 권한 (USER / ADMIN / GUEST)
        );
    }

    /**
     * 분기 처리 -> 맞는 소셜 타입을 반환하는 메서드
     * @param registrationId
     * @return
     */
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
     * 소셜 타입과 attributes에 있는 소셜 로그인의 식별값 id를 통해 사용자를 찾아 반환하는 메서드
     * 사용자가 존재한다면 그대로 반환하고 존재하지 않는다면 saveUser()를 호출해 회원을 저장한다.
     * @param attributes
     * @param socialType
     * @return
     */
    private User getUser(OAuthDTO attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialToken(socialType, attributes.getOAuth2UserInfo().getToken()).orElse(null);

        if(findUser == null) {
            return saveUser(attributes, socialType);
        }

        return findUser;
    }

    /**
     * OAuthDTO의 toEntity() 메서드를 통해 빌더로 User 객체를 생성한 후 반환한다.
     * 생성된 User 객체를 DB에 저장한다. (이러면 DB 내 사용자의 데이터에는 socialType과 socialToken, email (임의의 중복 없는 랜덤값), role만 존재한다.)
     * @param att
     * @param socialType
     * @return
     */
    private User saveUser(OAuthDTO att, SocialType socialType) {
        User createdUser = att.toEntity(socialType, att.getOAuth2UserInfo());
        return userRepository.save(createdUser);
    }
}
