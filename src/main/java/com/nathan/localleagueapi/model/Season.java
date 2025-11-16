package com.nathan.localleagueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Season {
    private int year;
    private String alias;
    private String id;
    private Status status;
}
