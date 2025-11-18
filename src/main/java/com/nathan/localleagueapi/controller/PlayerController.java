package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.Player;
import com.nathan.localleagueapi.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
