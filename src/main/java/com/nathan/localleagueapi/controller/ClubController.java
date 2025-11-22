package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.club.ClubStatics;
import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.service.ClubService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clubs")
@AllArgsConstructor
public class ClubController {

    private final ClubService service;
    private static final Logger log = LoggerFactory.getLogger(SeasonController.class);

    @GetMapping("")
    public ResponseEntity<List<Club>> getAllClubs(){
        try{
            List<Club> clubs = service.getAllClub();
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mini")
    public ResponseEntity<List<ClubMinimumInfo>> getAllClubsMinimalInfo(){
        try{
            List<ClubMinimumInfo> clubs = service.getAllClubsMinimalInfo();
            return new ResponseEntity<>(clubs, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{clubId}/players")
    public ResponseEntity<List<Player>> getClubPlayers(@PathVariable String clubId){
        try{
            List<Player> players = service.getClubPlayers(clubId);
            return new ResponseEntity<>(players, HttpStatus.OK);
        }  catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{clubId}")
    public ResponseEntity<Club> getClubById(@PathVariable UUID clubId){
        try{
            Club club = service.getClubById(clubId);
            return new ResponseEntity<>(club, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<List<Club>> updateOrCreateClubs(@RequestBody List<Club> clubs){
        try{
            List<Club> editedOrCreatedClubs = service.updateOrCreateClubs(clubs);
            return new ResponseEntity<>(editedOrCreatedClubs, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{clubId}/players")
    public ResponseEntity<List<Player>> removePlayerFromClub(@PathVariable String clubId, @RequestBody List<Player> players){
        try{
            List<Player> removedPlayers = service.removePlayers(players, clubId);
            return new ResponseEntity<>(removedPlayers, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{clubId}/players")
    public ResponseEntity<List<Player>> attachPlayerToCLub(@PathVariable String clubId, @RequestBody List<Player> players){
        try {
            List<Player> allPlayers = service.attachPlayers(players, clubId);
            return new ResponseEntity<>(allPlayers, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/statistics/{seasonYear}")
    public ResponseEntity<List<ClubStatics>> getClubStats(@PathVariable String seasonYear, @RequestParam(required = false) boolean hasToBeClassified){
        try{
            System.out.println(hasToBeClassified);
            List<ClubStatics> stats = service.getAllClubStats(hasToBeClassified, seasonYear);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
