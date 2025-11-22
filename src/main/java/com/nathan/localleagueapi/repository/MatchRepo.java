package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.model.match.Match;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
