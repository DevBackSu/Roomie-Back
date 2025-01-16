package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RankDTO { // 랭킹 반환에 사용하는 DTO

    private List<String> rank;   // 1순위

    @Override
    public String toString() {
        return "RankDTO [rank=" + rank + "]";
    }
}
