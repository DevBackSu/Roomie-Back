package com.example.roomie.Auth;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuthAttribute {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    @Builder
    public OAuthAttribute(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this. nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    /**
     * of() : OAuth2User에서 반환하는 사용자 정보를 받아서 변환함
     * -> OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값을 하나하나 변환해야 함
     */
    public static OAuthAttribute of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google" :
                return ofGoogle(userNameAttributeName, attributes);
//            case "kakao" :
//                return ofKakao(userNameAttributeName, attributes);
//            case "naver" :
//                return ofNaver(userNameAttributeName, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuthAttribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * OAuth에서 엔티티를 생성하는 시점은 항상 처음 가입할 때이기 때문에 기본 권한으로 Guest가 저장됨
     * @return
     */
    public User toEntity() {
        return User.builder()
                .nickname(name)   // 인증한 별명을 User 객체의 nickname에 저장
                .email(email)     // 인증한 이메일을 User 객체의 email에 저장
                .role(Role.GUEST) // 처음 인증한 사용자는 GUEST로 저장
                .build();
    }
}
