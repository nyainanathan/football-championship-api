package com.nathan.localleagueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewGoal {
    private String clubId;
    private String scorerIdentifier;
    private int minuteOfGoal;
}
