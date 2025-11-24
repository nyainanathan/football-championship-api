package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.dto.MatchFilter;
import com.nathan.localleagueapi.dto.MatchRawData;
import com.nathan.localleagueapi.dto.NewGoal;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.service.MatchService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/matches")
public class MatchController {
    private MatchService service;

    @GetMapping("/{seasonYear}")
    public ResponseEntity<List<Match>> getMatches(@PathVariable String seasonYear, MatchFilter filters) {
        try{
            List<Match> matches = service.getSeasonMatch(seasonYear, filters);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Match> updateMatchStatus(@PathVariable String id, @RequestBody Map<String, Status> matchStatus) {
        try{
            Match updatedMatch = service.updateMatchStatus(id, matchStatus);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/goals")
    public ResponseEntity<Match> addGoal(@PathVariable String id, @RequestBody List<NewGoal> goals) {
        try{
            Match updatedMatch = service.addGoal(id, goals);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
