package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.dto.MatchFilter;
import com.nathan.localleagueapi.dto.MatchRawData;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.model.match.MatchClub;
import com.nathan.localleagueapi.model.match.Scorer;
import com.nathan.localleagueapi.model.player.PlayerMinimumInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MatchRepo {
    private final DataSource  dataSource;
    private final ClubRepo clubRepo;

    public void createMatch(List<Match> matches, String season) throws SQLException {
        String sql = "INSERT INTO matches (club_home, club_away, stadium, season,match_date) VALUES (?::uuid, ?::uuid, ?, ?, ?::timestamp)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Match match : matches) {
                preparedStatement.setString(1, match.getHomeClub().getId());
                preparedStatement.setString(2, match.getAwayClub().getId());
                preparedStatement.setString(3, match.getStadium());
                preparedStatement.setString(4, season);
                preparedStatement.setString(5, String.valueOf(match.getMatchDate()));
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }

    }

    public List<Scorer> getScorers(String matchId, String teamId)  throws SQLException {
        List<Scorer> scorers = new ArrayList<>();
        try{
            String sql = "select g.*, p.id as player_id, p.name, p.number from goals as g inner join players as p on g.player_id = p.id where g.match_id = ?::uuid and g.club_id = ?::uuid";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, matchId);
            preparedStatement.setString(2, teamId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                scorers.add(new Scorer(
                        new PlayerMinimumInfo(
                             rs.getString("player_id"),
                             rs.getString("name"),
                             rs.getInt("number")
                        ),
                        rs.getInt("minute_of_goal"),
                        rs.getBoolean("own_goal"),
                        rs.getString("club_id")
                ));
            }
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
        return scorers;
    }

    public Match getOneMatch(String matchId) throws SQLException {
        String sql = "SELECT * FROM matches WHERE id = ?::uuid";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, matchId);
            ResultSet rs = stmt.executeQuery();

            MatchRawData matchRawData = null;

            if(rs.next()){
                matchRawData = new MatchRawData(
                    rs.getString("id"),
                        rs.getString("club_home"),
                        rs.getString("club_away"),
                        rs.getString("stadium"),
                        Instant.parse(rs.getString("match_date")),
                        Status.valueOf(rs.getString("actual_status")),
                        rs.getString("season")
                );
            }

            conn.close();

            MatchClub homeClub = new MatchClub(clubRepo.getOneClubMinimumInfo(matchRawData.getClubHomeId()));
            MatchClub awayClub = new MatchClub(clubRepo.getOneClubMinimumInfo(matchRawData.getClubAwayId()));

            List<Scorer> homeScorers = this.getScorers(matchRawData.getId(), homeClub.getId());
            List<Scorer> awayScorers = this.getScorers(matchRawData.getId(), awayClub.getId());

            homeClub.setScorers(homeScorers);
            awayClub.setScorers(awayScorers);

            homeClub.setScore(homeScorers.size());
            awayClub.setScore(awayScorers.size());

            return new Match(
                    matchRawData.getId(),
                    homeClub,
                    awayClub,
                    matchRawData.getStadium(),
                    matchRawData.getMatchDate(),
                    matchRawData.getActualStatus()
            );

        } catch (Exception e){
            throw e;
        }
    }

    public List<Match> getSeasonMatch(String season, MatchFilter filters) throws SQLException {
        List<Match> matches = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM matches where season = ? ");

        System.out.println(filters.toString());

        if(filters.getMatchAfter() != null){
            sql.append(" and match_date > ?::timestamp ");
        }
        if(filters.getMatchBeforeOrEquals() != null){
            sql.append(" and match_date <= ?::timestamp ");
        }
        if(filters.getMatchStatus() != null){
            sql.append(" and actual_status = ?::status_enum ");
        }
        System.out.println(sql.toString());
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            stmt.setString(1, season);
            int filter = 2;

            if(filters.getMatchAfter() != null){
                System.out.println("gotMatchAfter");
                stmt.setString(filter, filters.getMatchAfter().toString());
                filter++;
            }

            if(filters.getMatchBeforeOrEquals() != null){
                System.out.println("gotMatchBefore");
                stmt.setString(filter, String.valueOf(Timestamp.from(Instant.parse(filters.getMatchBeforeOrEquals().toString()))));
                filter++;
            }

            if(filters.getMatchStatus() != null){
                System.out.println("gotMatchStatus");
                stmt.setString(filter, filters.getMatchStatus().toString());
            }

            ResultSet rs = stmt.executeQuery();

            List<MatchRawData> matchData = new  ArrayList<>();

            List<ClubMinimumInfo> clubs = clubRepo.getAllClubsMinimumInfo();

            while(rs.next()){
                matchData.add(
                        new  MatchRawData(
                                rs.getString("id"),
                                rs.getString("club_home"),
                                rs.getString("club_away"),
                                rs.getString("stadium"),
                                Instant.parse(rs.getString("match_date").substring(0,10) + "T" + rs.getString("match_date").substring(11,19) + ".00Z"),
                                Status.valueOf(rs.getString("actual_status")),
                                rs.getString("season")
                        )
                );
            }

            conn.close();
            for(MatchRawData data :  matchData){
                //Home team
                MatchClub homeClub = new MatchClub(clubRepo.getOneClubMinimumInfo(data.getClubHomeId()));
                MatchClub awayClub = new MatchClub(clubRepo.getOneClubMinimumInfo(data.getClubAwayId()));

                List<Scorer> homeScorers = this.getScorers(data.getId(), homeClub.getId());
                System.out.println("Breakpoint");
                List<Scorer>  awayScorers = this.getScorers(data.getId(), awayClub.getId());

                homeClub.setScorers(homeScorers);
                awayClub.setScorers(awayScorers);

                homeClub.setScore(homeScorers.size());
                awayClub.setScore(awayScorers.size());

                matches.add(
                        new Match(
                            data.getId(),
                                homeClub,
                                awayClub,
                                data.getStadium(),
                                data.getMatchDate(),
                                data.getActualStatus()
                        )
                );

                for(Match m : matches){
                    System.out.println(m.toString());
                }
            }


            return filters.getClubPlayingName() != null ?  matches.stream()
                    .filter(match -> match.getAwayClub().getName().contains(filters.getClubPlayingName()) || match.getHomeClub().getName().contains(filters.getClubPlayingName()))
                    .toList() : matches;

        } catch (Exception e){
            throw e;
        }
    }
}
