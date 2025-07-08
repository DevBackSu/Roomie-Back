package com.example.roomie.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_check_id", unique = true, nullable = false)
    private Long postCheckId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false) -> 이렇게 설정하면 insert도 안 되고 update도 안 됨
    @JsonIgnore
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @Column(name = "write_dtm")
    private String writeDtm;

}
