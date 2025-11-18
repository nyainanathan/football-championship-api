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

    public Player createPlayer(Player player) throws SQLException {
        String sql = "INSERT INTO players (name, number, position, nationality, age, club_id) VALUES (?, ?, ?, ?, ?, null) returning *";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getNumber());
            stmt.setString(3, String.valueOf(player.getPosition()));
            stmt.setString(4, player.getNationality());
            stmt.setInt(5, player.getAge());
            ResultSet rs =  stmt.executeQuery();
            if(rs.next()){
                Player newPlayer =  playerRowMapper.map(rs);
                conn.close();
                return newPlayer;
            }

        } catch (Exception e){
            throw e;
        }
        return null;
    }

    public Player updatePlayer(Player player) throws SQLException {
        String sql = "UPATE players SET name = ? , number = ? , position = ?, age = ? WHERE id = ?";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getNumber());
            stmt.setString(3, String.valueOf(player.getPosition()));
            stmt.setInt(4, player.getAge());
            stmt.setString(5, player.getId());
            ResultSet rs =  stmt.executeQuery();

            if(rs.next()){
                conn.close();
                return playerRowMapper.map(rs);
            }
        } catch (Exception e){
            throw e;
        }
        return null;
    }

    //For stats

}
