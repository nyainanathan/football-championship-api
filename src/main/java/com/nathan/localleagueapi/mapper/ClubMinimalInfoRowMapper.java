package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.dto.ClubMinimumInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClubMinimalInfoRowMapper {
    public ClubMinimumInfo map(ResultSet resultSet) throws SQLException {
        return new ClubMinimumInfo(
            resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getString("acronym")
        );
    }
}
