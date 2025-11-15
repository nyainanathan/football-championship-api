package com.nathan.localleagueapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class Match {
    private String id;
    private Club homeClub;
    private Club AwayClub;
    private String stadium;
    private Instant matchaDate;
    private Status  status;
}
