package com.nathan.localleagueapi.models;

public class ClubStatics extends  Club {
    private int rankingPoint;
    private int scoredGoals;
    private int concedeGoals;
    private int differenceGoals;
    private int cleanSheetNumber;

    public ClubStatics(String id, String name, String acronym, int yearCreation, String stadium, Coach coach) {
        super(id, name, acronym, yearCreation, stadium, coach);
    }
}
