package com.nathan.localleagueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String id;
    private String name;
    private String nationality;
    private PlayerPosition position;
    private int age;
    private int number;
    private Club club;

    public Player(String id, String name, String nationality, PlayerPosition position, int age, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.position = position;
        this.nationality = nationality;
        this.age = age;
    }

    public Player(String id, String name, String nationality, PlayerPosition position, int age) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.position = position;
        this.age = age;
    }
}
