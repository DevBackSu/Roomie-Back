package com.example.roomie.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RankDTO { // 랭킹 반환에 사용하는 DTO

    private String local;


    public String toString() {
        return local;
    }
}
