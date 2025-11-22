package com.nathan.localleagueapi.model.match;

import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Match {
    private String id;
    private MatchClub homeClub;
    private MatchClub AwayClub;
    private String stadium;
    private Instant matchaDate;
    private Status status;
}
