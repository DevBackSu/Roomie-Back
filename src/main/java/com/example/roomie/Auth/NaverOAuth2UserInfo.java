package com.example.roomie.Auth;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * 네이버의 유저 정보 (attributes)의 response json 예시는 아래와 같음
     * {
     * "resultcode": "00",
     * "message": "success",
     * "response": {
     * "email": "openapi@naver.com",
     * "nickname": "OpenAPI",
     * "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
     * "age": "나이",
     * "gender": "성별",
     * "id": "식별값",
     * "name": "오픈 API",
     * "birthday": "생일"
     * }
     * }
     * 네이버는 response 키로 attributes가 감싸져 있기 때문에 get으로 꺼내야 한다.
     * get으로 꺼낸 값은 Object 타입이기 때문에 (String)으로 캐스팅해 반환해야 한다.
     *
     * @return
     */
    @Override
    public String getToken() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        return (String) response.get("name");
    }
}
