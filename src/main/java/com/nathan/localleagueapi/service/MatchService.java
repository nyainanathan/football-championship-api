package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.model.match.MatchClub;
import com.nathan.localleagueapi.repository.ClubRepo;
import com.nathan.localleagueapi.repository.MatchRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class MatchService {
    private ClubRepo clubRepo;
    private MatchRepo matchRepo;

    public List<Match> createSeasonMatch(String season){
        List<Match> seasonMatch = new ArrayList<>();
        List<ClubMinimumInfo> clubs = clubRepo.getAllClubsMinimumInfo();
        List<Club> clubsFullInfo = clubRepo.getAllClubs();
        StringBuilder seasonStart = new StringBuilder();
        seasonStart.append(season + "-08-15T18:00:00Z");
        Instant start = Instant.parse(seasonStart.toString());

        int teamCount = clubs.size();
        int rounds = teamCount - 1;
        int matchesPerRound = teamCount / 2;

        ClubMinimumInfo fixedClub = clubs.get(0);
        Club fixedFullInfo = clubsFullInfo.get(0);
        List<ClubMinimumInfo> rotating = new ArrayList<>(clubs.subList(1, teamCount));
        List<Club> rotatingFullInfo = clubsFullInfo.subList(1, teamCount);

        List<Instant> matchDates = new ArrayList<>();
        matchDates.add(start);
        System.out.println(rounds);
        for(int i = 1 ; i < rounds * 2; i++){
            if(i%5 ==0)
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(21)));
            else if (i%8 == 0)
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(31)));
            else
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(14)));
        }

        for(Instant d : matchDates){
            System.out.println(d);
        }

        for(int round = 0; round < rounds; round++){
            seasonMatch.add(
                    new Match(
                            String.valueOf(UUID.randomUUID()),
                            new MatchClub(
                                    fixedClub.getId(),
                                    fixedClub.getName(),
                                    fixedClub.getAcronym()
                            ),
                            new MatchClub(
                                rotating.getLast().getId(),
                                    rotating.getLast().getName(),
                                    rotating.getLast().getAcronym()
                            ),
                            fixedFullInfo.getStadium(),
                            matchDates.get(round),
                            Status.NOT_STARTED

                    )
            );

            for(int i = 0 ; i < matchesPerRound - 1 ; i++){
                ClubMinimumInfo homeClub = rotating.get(i);
                ClubMinimumInfo awayClub = rotating.get(rotating.size()-2-i);
                seasonMatch.add(
                        new Match(
                                String.valueOf(UUID.randomUUID()),
                                new MatchClub(
                                        rotating.get(i).getId(),
                                        rotating.get(i).getName(),
                                        rotating.get(i).getAcronym()
                                ),
                                new MatchClub(
                                        rotating.get(rotating.size() - 2 -1).getId(),
                                        rotating.get(rotating.size() - 2 -1).getName(),
                                        rotating.get(rotating.size() - 2 -1).getAcronym()
                                ),
                                rotatingFullInfo.get(i).getStadium(),
                                matchDates.get(round),
                                Status.NOT_STARTED

                        )
                );


            }

            rotating.addFirst(rotating.removeLast());
            rotatingFullInfo.addFirst(rotatingFullInfo.removeLast());
        }
        List<Match> secondLegMatches = new ArrayList<>();
            int i = 1;
            int counter = 1;
        System.out.println(rounds + " rounds");
            for(Match match : seasonMatch){
                String thisMatchStadium = clubsFullInfo.stream()
                                .filter(c -> c.getId().equals(match.getAwayClub().getId()))
                                        .map(Club::getStadium)
                                                .findFirst().orElse(null);
                secondLegMatches.add(new Match(
                        String.valueOf(UUID.randomUUID()),
                        match.getAwayClub(),
                        match.getHomeClub(),
                        thisMatchStadium,
                        matchDates.get(rounds + i  - 1),
                        Status.NOT_STARTED

                ));
                counter++;
                if(counter%5 == 0){
                    i ++;
                }
            }

        seasonMatch.addAll(secondLegMatches);
        System.out.println(seasonMatch.size());
        return seasonMatch;

    }
}
