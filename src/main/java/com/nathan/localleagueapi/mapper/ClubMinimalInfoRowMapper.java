package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ClubMinimalInfoRowMapper {
    public ClubMinimumInfo map(ResultSet resultSet) throws SQLException {
        return new ClubMinimumInfo(
            resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("acronym")
        );
    }
}
