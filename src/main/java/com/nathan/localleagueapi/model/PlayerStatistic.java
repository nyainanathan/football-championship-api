package com.nathan.localleagueapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerStatistic {
    private int scoredGoals;
    private PlayingTime playingTime;
}
