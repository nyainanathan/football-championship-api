package com.nathan.localleagueapi.dto;

import com.nathan.localleagueapi.model.Status;

import java.time.Instant;

public class MatchRawData {
    private String id;
    private String clubHomeId;
    private String clubAwayId;
    private String stadium;
    private Instant matchDate;
    private Status acrualStatus;
    private String season;
}
