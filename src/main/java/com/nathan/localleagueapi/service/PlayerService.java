package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.Player;
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
}
