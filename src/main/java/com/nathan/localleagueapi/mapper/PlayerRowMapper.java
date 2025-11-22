package com.nathan.localleagueapi.mapper;

import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.model.player.PlayerPosition;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerRowMapper {

    public Player map(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("id"),
                rs.getString("name"),
                rs.getInt("number"),
                PlayerPosition.valueOf(rs.getString("position")),
                rs.getString("nationality"),
                rs.getInt("age")
        );
    }
}
