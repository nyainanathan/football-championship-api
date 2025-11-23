package com.nathan.localleagueapi.model.match;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({
        "id", "homeClub", "awayClub", "stadium", "matchDate", "status"
})
public class Match {
    private String id;
    private MatchClub homeClub;
    private MatchClub AwayClub;
    private String stadium;
    private Instant matchDate;
    private Status status;

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", homeClub=" + homeClub +
                ", AwayClub=" + AwayClub +
                ", stadium='" + stadium + '\'' +
                ", matchDate=" + matchDate +
                ", status=" + status +
                '}';
    }
}
