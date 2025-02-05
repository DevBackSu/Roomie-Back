package com.example.roomie.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CHARACTER")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long characterId;

    @Column
    private String character;

    public String toString() {
        return "\ncharacter id : " + characterId + "\ncharacter : " + character + "\n";
    }

}
