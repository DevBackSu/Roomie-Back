package com.example.roomie.DTO;

import lombok.Builder;
import lombok.Getter;

/**
 * 소셜별 필요한 데이터가 달라서 소셜별로 데이터 분기 처리를 하는 DTO 클래스
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

}
