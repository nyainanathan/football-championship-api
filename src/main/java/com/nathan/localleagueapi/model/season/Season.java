package com.nathan.localleagueapi.model.season;

import com.nathan.localleagueapi.model.Status;
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
