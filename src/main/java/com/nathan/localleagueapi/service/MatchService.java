package com.nathan.localleagueapi.service;

import com.nathan.localleagueapi.dto.ClubStat;
import com.nathan.localleagueapi.dto.MatchFilter;
import com.nathan.localleagueapi.dto.MatchRawData;
import com.nathan.localleagueapi.dto.NewGoal;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.model.match.MatchClub;
import com.nathan.localleagueapi.repository.ClubRepo;
import com.nathan.localleagueapi.repository.MatchRepo;
import com.nathan.localleagueapi.repository.PlayerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@AllArgsConstructor
@Service
public class MatchService {

    private ClubRepo clubRepo;
    private MatchRepo matchRepo;
    private PlayerRepo playerRepo;

    public List<Match> createSeasonMatch(String season) throws SQLException {
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

        for(int i = 1 ; i < rounds * 2; i++){
            if(i%5 ==0)
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(21)));
            else if (i%8 == 0)
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(31)));
            else
                matchDates.add(matchDates.get(i-1).plus(Duration.ofDays(14)));
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
        matchRepo.createMatch(seasonMatch, season);
        return seasonMatch;
    }

    public List<Match> getSeasonMatch(String season, MatchFilter filters) throws SQLException {
        return matchRepo.getSeasonMatch(season, filters);
    }

    public void updateStatsAfterMatch(Match match) throws SQLException {

        ClubStat awayStat = clubRepo.getOneClubStatics(match.getAwayClub().getId());

        ClubStat homeStat = clubRepo.getOneClubStatics(match.getHomeClub().getId());

        if(match.getHomeClub().getScore() == match.getAwayClub().getScore()) {
            homeStat.setRankingPoint(homeStat.getRankingPoint() + 1);
            awayStat.setRankingPoint(awayStat.getRankingPoint() + 1);
        } else if (match.getHomeClub().getScore() > match.getAwayClub().getScore()) {
            homeStat.setRankingPoint(homeStat.getRankingPoint() + 3 );
        } else {
            awayStat.setRankingPoint(awayStat.getRankingPoint() + 3 );
        }


        if(match.getAwayClub().getScore() == 0){
            homeStat.setCleanSheets(homeStat.getCleanSheets() + 1);
        } else {
            awayStat.setScoredGals(awayStat.getScoredGals() + match.getAwayClub().getScore());
            awayStat.adjustDifferenceGoals();
            homeStat.setConcededGoals(homeStat.getConcededGoals() + match.getAwayClub().getScore());
            homeStat.adjustDifferenceGoals();
        }
        if(match.getHomeClub().getScore() == 0){
            awayStat.setCleanSheets(awayStat.getCleanSheets() + 1);
        } else {
            homeStat.setScoredGals(homeStat.getScoredGals() + match.getHomeClub().getScore());
            homeStat.adjustDifferenceGoals();
            awayStat.setConcededGoals(awayStat.getConcededGoals() + match.getHomeClub().getScore());
            awayStat.adjustDifferenceGoals();
        }


        clubRepo.updateStats(homeStat);

        clubRepo.updateStats(awayStat);
    }

    @Transactional
    public Match updateMatchStatus(String id, Map<String, Status> matchStatus) throws SQLException {

        Status status = matchRepo.getOneMatchStatus(id);

        if(matchStatus.get("status") == Status.STARTED && status == Status.NOT_STARTED ){
            matchRepo.startMatch(id);

            return matchRepo.getOneMatch(id);
        } else if (matchStatus.get("status") == Status.FINISHED && status == Status.STARTED){
            matchRepo.finishMatch(id);
            Match match = matchRepo.getOneMatch(id);
            updateStatsAfterMatch(match);
            return matchRepo.getOneMatch(id);
        } else {
                throw new SQLException("You cannot edit a match with these status");
        }
    }

    public Match addGoal(String id, List<NewGoal> goals) throws SQLException {
        for(NewGoal goal : goals){
            boolean isOwnGoal = !Objects.equals(playerRepo.getClubId(goal.getScorerIdentifier()), goal.getClubId());
            matchRepo.addGoal(goal, isOwnGoal, id);
        }
        return matchRepo.getOneMatch(id);
    }

}
