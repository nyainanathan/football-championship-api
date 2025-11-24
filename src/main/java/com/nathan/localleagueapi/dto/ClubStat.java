package com.nathan.localleagueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClubStat {
    private String id;
    private int rankingPoint;
    private int scoredGals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheets;
    private String season;

    public void adjustDifferenceGoals() {
        this.setDifferenceGoals(this.getScoredGals() - this.getConcededGoals());
    }
}
