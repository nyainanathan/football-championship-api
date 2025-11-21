package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.model.ClubStatics;
import com.nathan.localleagueapi.model.Coach;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClubStatRowMapper {
    public ClubStatics map(ResultSet rs) throws SQLException {
        return new ClubStatics(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("acronym"),
                rs.getInt("year_creation"),
                rs.getString("stadium"),
                new Coach(
                        rs.getString("coach_name"),
                        rs.getString("coach_nationality")
                ),
                rs.getInt("ranking_point"),
                rs.getInt("scored_goals"),
                rs.getInt("conceded_goals"),
                rs.getInt("differences_goals"),
                rs.getInt("clean_sheets")
        );
    }
}
