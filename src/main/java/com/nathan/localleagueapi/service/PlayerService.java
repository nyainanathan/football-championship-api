package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.model.player.PlayerStatistic;
import com.nathan.localleagueapi.repository.PlayerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayerService {

    private PlayerRepo repo;

    public List<Player> getPlayers(){
        return repo.getAllPlayers();
    }

    public List<Player> createOrUpdatePlayers(List<Player> players) throws SQLException {

        for(Player player : players){
            try{


            Player playerBeforeUpdate = repo.getPlayerById(player.getId());

            if(playerBeforeUpdate == null){
                repo.createPlayer(player);
            } else {
                repo.updatePlayer(player);
            }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        return players;
    }

    //Stats
    public PlayerStatistic getStatForPlayerByYear(String playerId, String year) throws SQLException {
        return repo.getPlayerStatisticForSeason(playerId, year);
    }
}
