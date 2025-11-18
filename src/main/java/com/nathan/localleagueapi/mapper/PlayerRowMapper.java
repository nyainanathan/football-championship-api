package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.model.Player;
import com.nathan.localleagueapi.model.PlayerPosition;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRowMapper {

    public Player map(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("nationality"),
                PlayerPosition.valueOf(rs.getString("position")),
                rs.getInt("age"),
                rs.getInt("number")
        );
    }
}
