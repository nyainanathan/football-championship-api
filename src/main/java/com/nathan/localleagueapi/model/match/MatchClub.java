package com.nathan.localleagueapi.model.match;

import com.nathan.localleagueapi.model.club.CLubMinimumInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MatchClub extends CLubMinimumInfo {
    private int score;
    private List<Scorer> scorers;

    public MatchClub(String id, String name, String acronym, int score) {
        super(id, name, acronym);
        this.score = score;
        this.scorers = new ArrayList<>();
    }

    public MatchClub(String id, String name, String acronym, int score, List<Scorer> scorers) {
        super(id, name, acronym);
        this.score = score;
        this.scorers = scorers;
    }
}
