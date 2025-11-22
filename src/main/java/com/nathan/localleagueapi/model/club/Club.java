package com.nathan.localleagueapi.model.club;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Club  extends CLubMinimumInfo{

    private int yearCreation;
    private String stadium;
    private Coach coach;

    public Club(String id, String name, String acronym, int yearCreation, String stadium, Coach coach) {
        super(id, name, acronym);
        this.yearCreation = yearCreation;
        this.stadium = stadium;
        this.coach = coach;
    }
}
