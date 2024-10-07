package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RankDTO { // 랭킹 반환에 사용하는 DTO

    private String frist;   // 1순위
    private String second;  // 2순위
    private String third;   // 3순위
    private String fourth;  // 4순위
    private String fifth;   // 5순위

    @Override
    public String toString() {
        return "1 : " + frist + "\n2 : " + second + "\n3 : " + third + "\n4 : " + fourth + "\n5 : " + fifth;
    }
}
