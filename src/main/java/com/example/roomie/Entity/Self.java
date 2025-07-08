package com.example.roomie.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자가 protected로 제한됨 -> 외부에서 new User(); 불가능!
@AllArgsConstructor //builder 패턴을 사용할 때 매개변수가 없는 생성자 외 생성자가 존재할 수 있어서 모든 필드를 가진 생성자도 필요함
@Entity
@Builder // 빌더 패턴 적용
@Table(name = "SELF")
public class Self {
    // 생각해보니까 self_id가 필요하지 않음. user_id가 FK면서 PK면 됨 -> 수정 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "self_id")
    private Long selfId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "about_me")
    private String aboutMe;
}
