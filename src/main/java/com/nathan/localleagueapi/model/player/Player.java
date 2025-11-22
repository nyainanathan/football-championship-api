package com.nathan.localleagueapi.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String id;
    private String name;
    private int number;
    private PlayerPosition position;
    private String nationality;
    private int age;


}
