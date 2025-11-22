package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.dto.CreateSeason;
import com.nathan.localleagueapi.model.season.Season;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.service.SeasonService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/seasons")
public class SeasonController {
    private static final Logger log = LoggerFactory.getLogger(SeasonController.class);
    private SeasonService service;

    @GetMapping("")
    public ResponseEntity<List<Season>> getAllSeasons() {
        try{
            List<Season> seasons = service.getAllSeasons();
            return new ResponseEntity<>(seasons, HttpStatus.OK);
        } catch(Exception e){
            log.error("Failed to get seasons", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<List<Season>> createNewSeasons(@RequestBody List<CreateSeason> newSeasons) {
        try{
            List<Season> seasons = service.createSeasons(newSeasons);
            return new ResponseEntity<>(seasons, HttpStatus.CREATED);
        } catch (Exception e){
            log.error("Failed to create seasons", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{seasonYear}/status")
    public ResponseEntity<Season> updateSeasonStatus(@PathVariable String seasonYear, @RequestBody Map<String, Status> updatedYear){
        try{
            Season updatedSeason = service.changeSeasonStatus(seasonYear, updatedYear.get("status"));
            return new  ResponseEntity<>(updatedSeason, HttpStatus.OK);
        } catch (Exception e){
            log.error("Failed to update season status", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
