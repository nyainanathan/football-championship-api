package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.model.player.PlayerStatistic;
import com.nathan.localleagueapi.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private PlayerService service;

    @GetMapping("")
    public ResponseEntity<List<Player>> getAllPlayers(){
        try{
            List<Player> players = service.getPlayers();
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<List<Player>> createOrUpdatePlayers(@RequestBody List<Player> players){
        try {
            List<Player> updatedOrCreatedPlayers = service.createOrUpdatePlayers(players);
            return new  ResponseEntity<>(updatedOrCreatedPlayers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/statistics/{seasonYear}")
    public ResponseEntity<PlayerStatistic> getOnePlayerStatistic(@PathVariable String id, @PathVariable String seasonYear) throws SQLException {
        try{
            PlayerStatistic stat = service.getStatForPlayerByYear(id, seasonYear);
            return new  ResponseEntity<>(stat, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
