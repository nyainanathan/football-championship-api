package com.nathan.localleagueapi.model.match;

import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.model.player.PlayerMinimumInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Scorer {
    private PlayerMinimumInfo player;
    private int minuteOfGoal;
    private boolean ownGoal;
}
