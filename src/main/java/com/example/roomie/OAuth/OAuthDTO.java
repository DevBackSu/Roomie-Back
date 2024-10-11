package com.example.roomie.OAuth;

import com.example.roomie.Entity.Role;
import com.example.roomie.Entity.SocialType;
import com.example.roomie.Entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/**
 * 소셜별 필요한 데이터가 달라서 소셜별로 데이터 분기 처리를 하는 DTO 클래스
 * 소셜에서 반환하는 사용자의 정보인 Attributes를 받아 처리하는 클래스다.
 */
@Getter
public class OAuthDTO {

    private String nameKey; // OAuth2 로그인 진행 시 키가 되는 필드 값 (PK)
    private OAuth2UserInfo oAuth2UserInfo; // 소셜 타입별 로그인 유저 정보 (추상클래스)

    @Builder
    private OAuthDTO(String nameKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameKey = nameKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /**
     * SocialType에 맞는 메서드를 호출해 OAuthDTO 객체를 반환
     */
    public static OAuthDTO of(SocialType socialType, String userNameAttributeName, Map<String,Object> attributes) {
        if(socialType == SocialType.Google) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        if(socialType == SocialType.Kakao) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofNaver(userNameAttributeName, attributes);
    }

    /**
     * 파라미터로 들어온 socialType별로 분기 처리해서 각 소셜 타입에 맞게 OAuthAttributes를 생성함
     * @param userNameAttributeName
     * @param attributes
     * @return
     */
    public static OAuthDTO ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthDTO.builder()
                .nameKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthDTO ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthDTO.builder()
                .nameKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthDTO ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthDTO.builder()
                .nameKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of 메소드로 OAuthAttributes 객체가 생성되고 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태임
     * OAuth2UserInfo에서 식별값인 socialToken, name, imgUrl을 가져와 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        Long id = Long.parseLong(UUID.randomUUID().toString());
        return User.builder()
                .socialType(socialType)
                .socialToken(oAuth2UserInfo.getToken())
                .id(id)
                .email(UUID.randomUUID() + "@socialUser.com")
                .role(Role.GUEST)
                .build();
    }

}
