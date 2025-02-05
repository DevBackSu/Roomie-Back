package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserCharacterDTO {
    private Long userId; // 사용자 ID
    private List<String> features; // 사용자가 선택한 특징 목록
}
