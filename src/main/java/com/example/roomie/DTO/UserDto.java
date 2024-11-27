package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDTO {
    private Long userId;
    private String socialId;

    private String token;

}
