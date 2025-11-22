package com.nathan.localleagueapi.model.player;

import com.nathan.localleagueapi.model.DurationUnit;
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
