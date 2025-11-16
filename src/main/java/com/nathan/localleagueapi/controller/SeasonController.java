package com.nathan.localleagueapi.controller;

import com.nathan.localleagueapi.model.Season;
import com.nathan.localleagueapi.service.SeasonService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/seasons")
public class SeasonController {
    private static final Logger log = LoggerFactory.getLogger(SeasonController.class);
    private SeasonService service;

    @GetMapping("/")
    public ResponseEntity<List<Season>> getAllSeasons() {
        try{
            List<Season> seasons = service.getAllSeasons();
            return new ResponseEntity<>(seasons, HttpStatus.OK);
        } catch(Exception e){
            log.error("Failed to get seasons", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
