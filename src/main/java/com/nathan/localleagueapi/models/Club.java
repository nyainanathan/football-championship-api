package com.nathan.localleagueapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Club {
    private String id;
    private String name;
    private String acronym;
    private int yearCreation;
    private String stadium;
    private Coach coach;
}
