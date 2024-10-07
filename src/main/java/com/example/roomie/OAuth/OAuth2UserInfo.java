package com.example.roomie.OAuth;

import java.util.Map;

/**
 * 소셜 타입별로 사용자의 정보를 가지는 추상 클래스
 * OAuth2UserInfo 추상클래스를 상속 받아 각 소셜 타입의 사용자 정보 클래스를 구현
 */
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;  // 추상클래스를 상속 받는 클래스에서만 사용할 수 있게 하기 위해 protected 사용

    /**
     * 생성자.
     * 파라미터로 각 소셜 타입별 사용자 정보인 attributes를 주입 받아서 소셜 타입별 사용자 정보 클래스에게 소셜 타입에 맞는 attributes를 가지도록 함
     * @param attributes
     */
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getToken(); // 소셜 식별 값

    // 아래에는 서비스에서 사용하고 싶은 소셜값을 가져오는 메소드를 추가 생성해 소셜에서 제공하는 정보를 가져올 수 있음 (각 소셜마다 제공하는 정보를 확인한 후 작성하기)
}
