package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.Player;
import com.nathan.localleagueapi.model.PlayerStatistic;
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
        System.out.println("andhea");
        for(Player player : players){
            try{

            System.out.println("mety aloha iy eto");
            System.out.println(player.getName());
            Player playerBeforeUpdate = repo.getPlayerById(player.getId());
            System.out.println(playerBeforeUpdate);
            if(playerBeforeUpdate == null){
                repo.createPlayer(player);
                System.out.println("Creation");
            } else {
                System.out.println("Updating");
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
