package com.nathan.localleagueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayingTime {
    private int value;
    private DurationUnit durationUnit;
}
