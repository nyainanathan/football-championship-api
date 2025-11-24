package com.nathan.localleagueapi.dto;

import com.nathan.localleagueapi.model.DurationUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerStat {
    private String playerId;
    private String season;
    private int scoredGoals;
    private int playingTime;
    private DurationUnit durationUnit;

    public void addGoal(){
        this.setScoredGoals(this.getScoredGoals() + 1);
    }
    public void addGoal(int goals){
        this.setScoredGoals(this.getScoredGoals() + goals);
    }

    public void addPlayingTime(int time){
        this.setPlayingTime(this.getPlayingTime() + time);
    }

    @Override
    public String toString() {
        return "PlayerStat{" +
                "playerId='" + playerId + '\'' +
                ", season='" + season + '\'' +
                ", scoredGoals=" + scoredGoals +
                ", playingTime=" + playingTime +
                ", durationUnit=" + durationUnit +
                '}';
    }
}
