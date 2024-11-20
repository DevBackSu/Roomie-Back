package com.example.roomie.Auth;

import com.example.roomie.Entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * User 클래스에 직렬화 코드를 넣으면 직렬화 대상에 자식들까지 포함되어 성능 이슈나 부수 효과가 발생할 수 있다.
 * 때문에 직렬화 기능을 가진 세션 DTO를 추가하는 것이 운영 및 유지보수에 용이하다.
 */
@Getter
public class SessionUser implements Serializable {   // 인증된 사용자 정보만 필요함
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getNickname();
        this.email = user.getEmail();
    }
}
