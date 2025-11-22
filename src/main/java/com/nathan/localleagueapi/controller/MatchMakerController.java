package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.service.MatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/matchMaker")
public class MatchMakerController {

    private MatchService service;

    @PostMapping("/{seasonYear}")
    public ResponseEntity<List<Match>> createMatchForASeason(@PathVariable String seasonYear){
        try{
            List<Match> matches = service.createSeasonMatch(seasonYear);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
