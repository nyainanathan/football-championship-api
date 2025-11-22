package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.dto.CreateSeason;
import com.nathan.localleagueapi.model.season.Season;
import com.nathan.localleagueapi.model.Status;
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

    public List<Season> createSeasons(List<CreateSeason> seasons){
        return repo.createSeasons(seasons);
    }

    public Season changeSeasonStatus(String year, Status newStatus) throws Exception {
        Status oldStatus = repo.getSeasonByYear(year).getStatus();
        if(oldStatus == Status.FINISHED && newStatus != Status.FINISHED){
            throw new Exception("A finished season cannot be updated anymore");
        } else if (oldStatus == Status.STARTED && newStatus == Status.NOT_STARTED)        {
            throw new Exception("A started season cannot be undone anymore");
        }
        return repo.updateSeasonStatus(year, newStatus);
    }
}
