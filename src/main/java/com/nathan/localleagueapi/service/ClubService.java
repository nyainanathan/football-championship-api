package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.Club;
import com.nathan.localleagueapi.repository.ClubRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepo repo;

    public List<Club> getAllClub(){
        return repo.getAllClubs();
    }
}
