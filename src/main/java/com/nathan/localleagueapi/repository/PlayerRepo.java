package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.mapper.PlayerRowMapper;
import com.nathan.localleagueapi.model.Player;
import com.nathan.localleagueapi.model.PlayerPosition;
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
public class PlayerRepo {

    private DataSource dataSource;
    private PlayerRowMapper playerRowMapper;

  public List<Player> getAllPlayers(){
      String sql = "SELECT * FROM players";
      List<Player> playerList = new ArrayList<>();

      try{
          Connection conn = dataSource.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery();
          while(rs.next()){
                playerList.add(playerRowMapper.map(rs));
          }
          conn.close();
          return playerList;
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
  }

    public Player getPlayerById(String id) throws SQLException {
        String sql = "SELECT * FROM players WHERE id = ?::uuid";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs =  stmt.executeQuery();

            if(rs.next()){
                conn.close();
                return playerRowMapper.map(rs);
            }
        } catch(Exception e){
            throw e;
        }
        return null;
    }
}
