package com.nathan.localleagueapi.model.club;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClubStatics extends Club {
    private int rankingPoint;
    private int scoredGoals;
    private int concedeGoals;
    private int differenceGoals;
    private int cleanSheetNumber;

    public ClubStatics(String id, String name, String acronym, int yearCreation, String stadium, Coach coach ) {
        super(id, name, acronym, yearCreation, stadium, coach);
    }

    public ClubStatics(String id, String name, String acronym, int yearCreation, String stadium, Coach coach, int rankingPoint, int scoredGoals, int concedeGoals, int differenceGoals, int cleanSheetNumber) {
        super(id, name, acronym, yearCreation, stadium, coach);
        this.rankingPoint = rankingPoint;
        this.scoredGoals = scoredGoals;
        this.concedeGoals = concedeGoals;
        this.differenceGoals = differenceGoals;
        this.cleanSheetNumber = cleanSheetNumber;
    }
}
