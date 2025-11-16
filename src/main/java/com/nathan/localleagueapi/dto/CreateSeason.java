package com.nathan.localleagueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateSeason {
    private int year;
    private String alias;
}
