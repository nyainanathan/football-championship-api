package com.nathan.localleagueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coach {
    private String id;
    private String name;
    private String nationality;
}
