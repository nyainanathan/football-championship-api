package com.nathan.localleagueapi.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PlayerStatistic {
    private int scoredGoals;
    private PlayingTime playingTime;
}
