package com.nathan.localleagueapi.model.match;

import com.nathan.localleagueapi.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Scorer {
    private Player player;
    private int minuteOfGoal;
    private boolean ownGoal;
}
