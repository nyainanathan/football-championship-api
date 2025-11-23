package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.model.match.Scorer;
import com.nathan.localleagueapi.model.player.PlayerMinimumInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MatchRepo {
    private final DataSource  dataSource;

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

    public List<Scorer> getScorers(String matchId)  throws SQLException {
        List<Scorer> scorers = new ArrayList<>();
        try{
            String sql = "select g.*, p.id as player_id, p.name, p.number from goals as g inner join players as p on g.player_id = p.id where g.match_id = ?";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, matchId);
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
        } catch (SQLException e){
            e.printStackTrace();
            throw e;
        }
        return scorers;
    }
}
