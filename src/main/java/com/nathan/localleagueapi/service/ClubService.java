package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.mapper.PlayerRowMapper;
import com.nathan.localleagueapi.model.Club;
import com.nathan.localleagueapi.model.Player;
import com.nathan.localleagueapi.repository.ClubRepo;
import com.nathan.localleagueapi.repository.CoachRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepo repo;
    private final CoachRepo coachRepo;

    public List<Club> getAllClub(){
        return repo.getAllClubs();
    }

    public Club getClubById(UUID id){
        Club  club = repo.getClubById(id);
        if(club == null){
            throw new RuntimeException("Club with id " + id + " not found");
        }
        return club;
    }

    public List<Player> getClubPlayers(String clubId){
        return repo.getClubPlayer(clubId);
    }

    public List<Club> updateOrCreateClubs(List<Club> clubs) throws Exception {

        for(Club club : clubs){
            Club sameClubBeforeUpdate = repo.getClubById(UUID.fromString(club.getId()));
            UUID coachId = coachRepo.getCoachIdByName(club.getCoach().getName());

            if(sameClubBeforeUpdate != null){
                repo.updateClub(club, coachId);
            } else {
                repo.createClub(club, coachId);
            }
        }
        return clubs;
    }
}
