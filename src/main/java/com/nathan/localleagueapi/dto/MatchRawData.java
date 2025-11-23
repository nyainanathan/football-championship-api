package com.nathan.localleagueapi.dto;

import com.nathan.localleagueapi.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class MatchRawData {
    private String id;
    private String clubHomeId;
    private String clubAwayId;
    private String stadium;
    private Instant matchDate;
    private Status actualStatus;
    private String season;

    @Override
    public String toString() {
        return "MatchRawData{" +
                "id='" + id + '\'' +
                ", clubHomeId='" + clubHomeId + '\'' +
                ", clubAwayId='" + clubAwayId + '\'' +
                ", stadium='" + stadium + '\'' +
                ", matchDate=" + matchDate +
                ", actualStatus=" + actualStatus +
                ", season='" + season + '\'' +
                '}';
    }
}
