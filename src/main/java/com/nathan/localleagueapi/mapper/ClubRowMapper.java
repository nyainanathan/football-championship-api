package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.Coach;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ClubRowMapper {
    public Club map(ResultSet rs) throws SQLException {
        return new Club(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("acronym"),
                rs.getInt("year_creation"),
                rs.getString("stadium"),
                new Coach(
                        rs.getString(8),
                        rs.getString(9)
                )
        );
    }
}
