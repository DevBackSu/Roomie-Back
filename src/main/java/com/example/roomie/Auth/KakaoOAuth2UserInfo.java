package com.example.roomie.Auth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    /**
     * 카카오의 유저 정보 (attributes)의 response json 예시는 아래와 같음
     * {
     *     "id": 식별값,
     *     "connected_at": "연결 날짜",
     *     "kakao_account": {
     *         // 프로필 또는 닉네임 동의 항목 필요
     *         "profile_nickname_needs_agreement": false,
     *         // 프로필 또는 프로필 사진 동의 항목 필요
     *         "profile_image_needs_agreement	": false,
     *         "profile": {
     *             // 프로필 또는 닉네임 동의 항목 필요
     *             "nickname": "홍길동",
     *             // 프로필 또는 프로필 사진 동의 항목 필요
     *             "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
     *             "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg",
     *             "is_default_image":false
     *         },
     *         // 이름 동의 항목 필요
     *         "name_needs_agreement":false,
     *         "name":"홍길동",
     *         // 카카오계정(이메일) 동의 항목 필요
     *         "email_needs_agreement":false,
     *         "is_email_valid": true,
     *         "is_email_verified": true,
     *         "email": "sample@sample.com",
     *         // 연령대 동의 항목 필요
     *         "age_range_needs_agreement":false,
     *         "age_range":"20~29",
     *         // 출생 연도 동의 항목 필요
     *         "birthyear_needs_agreement": false,
     *         "birthyear": "2002",
     *         // 생일 동의 항목 필요
     *         "birthday_needs_agreement":false,
     *         "birthday":"1130",
     *         "birthday_type":"SOLAR",
     *         // 성별 동의 항목 필요
     *         "gender_needs_agreement":false,
     *         "gender":"female",
     *         // 카카오계정(전화번호) 동의 항목 필요
     *         "phone_number_needs_agreement": false,
     *         "phone_number": "+82 010-1234-5678",
     *         // CI(연계정보) 동의 항목 필요
     *         "ci_needs_agreement": false,
     *         "ci": "${CI}",
     *         "ci_authenticated_at": "2019-03-11T11:25:22Z",
     *     },
     *     "properties":{
     *         "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
     *         ...
     *     }
     * }
     * 카카오의 식별값에 대한 key는 id이다.
     * 추가로, 카카오는 사용자의 추가적인 정보를 kakao_account 안의 profile 내에 존재한다. 때문에 get을 2번 사용해 데이터를 꺼내 사용하고 싶은 정보의 key로 해당 값을 꺼내야 한다.
     * @return
     */
    @Override
    public String getToken() {
        return String.valueOf(attributes.get("id")); // google과 달리 Long으로 반환되기 때문에 (String)으로 Casting할 수 없다. 때문에 어떠한 값을 넣어도 String 문자열로 변환해주는 String.valueOf()를 사용해야 한다.
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name"); // google과 달리 Long으로 반환되기 때문에 (String)으로 Casting할 수 없다. 때문에 어떠한 값을 넣어도 String 문자열로 변환해주는 String.valueOf()를 사용해야 한다.
    }
}
