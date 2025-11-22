package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.repository.ClubRepo;
import com.nathan.localleagueapi.repository.MatchRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    private ClubRepo clubRepo;
    private MatchRepo matchRepo;

}
