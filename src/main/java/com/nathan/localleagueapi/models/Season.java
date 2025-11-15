package com.nathan.localleagueapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Season {
    private String id;
    private int year;
    private String alias;
    private Status status;
}
