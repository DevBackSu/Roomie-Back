package com.example.roomie.OAuth;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * google의 유저 정보 (attributes)의 response json 예시는 아래와 같음
     * {
     *    "sub": "식별값",
     *    "name": "name",
     *    "given_name": "given_name",
     *    "picture": "https//lh3.googleusercontent.com/~~",
     *    "email": "email",
     *    "email_verified": true,
     *    "locale": "ko"
     * }
     * 때문에 식별값인 token값을 가지고 오기 위해서는 key가 sub인 값을 가져와야 함
     * @return
     */
    @Override
    public String getToken() {
        return (String) attributes.get("sub");
    }
}
