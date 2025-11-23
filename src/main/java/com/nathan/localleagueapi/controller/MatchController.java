package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.dto.MatchFilter;
import com.nathan.localleagueapi.dto.MatchRawData;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.service.MatchService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
