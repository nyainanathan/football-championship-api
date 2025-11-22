package com.nathan.localleagueapi.dto;

import com.nathan.localleagueapi.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MatchFilter {
    private Status matchStatus;
    private String clubPlayingName;
    private Instant matchAfter;
    private Instant matchBeforeOrEquals;
}
