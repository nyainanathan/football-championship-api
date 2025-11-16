package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.model.Season;
import com.nathan.localleagueapi.model.Status;
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
public class SeasonRepo {

    private final DataSource dataSource;

    public List<Season> getAllExistingSeasons(){
        String sql = "SELECT * FROM seasons";

        List<Season> seasons = new ArrayList<>();

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Season s = new Season(
                    rs.getInt("year"),
                        rs.getString("alias"),
                        rs.getString("id"),
                        Status.valueOf(rs.getString("status"))
                );
                seasons.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return seasons;
    }
}
