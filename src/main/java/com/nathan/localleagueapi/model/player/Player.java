package com.nathan.localleagueapi.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Player extends PlayerMinimumInfo{

    private PlayerPosition position;
    private String nationality;
    private int age;

    public Player(String id, String name, int number, PlayerPosition position, String nationality, int age) {
        super(id, name, number);
        this.position = position;
        this.nationality = nationality;
        this.age = age;
    }
}
