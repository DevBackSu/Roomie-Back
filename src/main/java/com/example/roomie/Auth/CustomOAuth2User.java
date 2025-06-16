package com.example.roomie.Auth;

import com.example.roomie.Entity.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Resource Server에서 제공하지 않는 정보를 가지고 있기 위해 구현
 */
@Getter
@Setter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;  // 최초 로그인 시 내 서비스는 사용자가 어떤 서비스에서 로그인 했는지 모르기 때문에 임시 이메일을 생성해 access token을 발급 받아 식별용으로 사용함
    private String name;
    private Role role;     // 최초 로그인 판단을 위해 필요. 처음 로그인 하는 사용자는 guest / 추가 정보를 입력한 사용자는 user로 설정
    private String socialToken;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey,
                            String email, String name, Role role, String socialToken) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.name = name;
        this.role = role;
        this.socialToken = socialToken;
    }

    public String getNickname() {
        return this.name;
    }
}
