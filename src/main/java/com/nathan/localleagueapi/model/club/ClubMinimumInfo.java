package com.nathan.localleagueapi.model.club;

import com.nathan.localleagueapi.model.match.Scorer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClubMinimumInfo{
    private final String id;
    private final String name;
    private final String acronym;

}
