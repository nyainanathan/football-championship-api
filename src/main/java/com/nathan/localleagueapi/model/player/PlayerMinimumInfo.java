package com.nathan.localleagueapi.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerMinimumInfo {
    private String id;
    private String name;
    private int number;
}
