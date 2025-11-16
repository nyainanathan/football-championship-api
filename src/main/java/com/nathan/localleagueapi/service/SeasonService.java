package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.Season;
import com.nathan.localleagueapi.repository.SeasonRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeasonService {

    private SeasonRepo repo;

    public List<Season>  getAllSeasons(){
        return repo.getAllExistingSeasons();
    }
}
