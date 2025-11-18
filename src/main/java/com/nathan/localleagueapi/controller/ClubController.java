package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.Club;
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
}
