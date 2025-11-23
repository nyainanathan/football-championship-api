package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.club.ClubStatics;
import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.repository.ClubRepo;
import com.nathan.localleagueapi.repository.CoachRepo;
import com.nathan.localleagueapi.repository.PlayerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.rowset.RowSetWarning;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ClubService {

    private final ClubRepo repo;
    private final CoachRepo coachRepo;
    private final PlayerRepo playerRepo;

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

    public List<ClubMinimumInfo> getAllClubsMinimalInfo(){
        return repo.getAllClubsMinimumInfo();
    }

    public List<Player> removePlayers(List<Player> players, String clubID) throws SQLException {
        for(Player player : players){
            String playerClub = playerRepo.getClubId(player.getId());
            System.out.println(playerClub);
            if(Objects.equals(playerClub, clubID)){
                repo.removePlayerFromClub(player.getId());
            } else if (playerClub == null){
                throw new RuntimeException("Player with id " + player.getId() + " not found");
            } else
                throw new RowSetWarning("Player is attached to another club");
        }
        return repo.getClubPlayer(clubID);
    }

    public List<Player> attachPlayers(List<Player> players, String clubID) throws SQLException {
        for(Player player : players){
            System.out.println("in the service");
            System.out.println("THis player id: " + player.getId());
            Player thisPlayer = playerRepo.getPlayerById(player.getId());
            System.out.println(thisPlayer);
            if(thisPlayer == null){
                System.out.println("This player does not exit bro");
                Player createdPlayer = playerRepo.createPlayer(player);
                repo.attachPlayerToClub(clubID, createdPlayer.getId());
            } else {
                System.out.println("This player does exist");
                String playerClub = playerRepo.getClubId(player.getId());
                System.out.println(playerClub);
                if (playerClub == null){
                    repo.attachPlayerToClub(clubID, player.getId());
                } else {
                    throw new  RuntimeException("Player is attached to another club");
                }
            }
        }
        return repo.getClubPlayer(clubID);
    }

    public List<ClubStatics> getAllClubStats(boolean hasToBeClassified, String season) throws SQLException {
        return repo.getAllClubsStats(hasToBeClassified, season);
    }

    public ClubMinimumInfo getOneClubMinimalInfo(String clubId) {
        return repo.getOneClubMinimumInfo(clubId);
    }
}
