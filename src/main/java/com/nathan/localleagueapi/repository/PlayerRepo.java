package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.dto.PlayerStat;
import com.nathan.localleagueapi.mapper.PlayerRowMapper;
import com.nathan.localleagueapi.model.*;
import com.nathan.localleagueapi.model.player.Player;
import com.nathan.localleagueapi.model.player.PlayerStatistic;
import com.nathan.localleagueapi.model.player.PlayingTime;
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
//                conn.close();
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
        String sql = "UPDATE players SET name = ? , number = ? , position = ?, age = ? WHERE id = ?";
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

    public String getClubId(String playerID) throws SQLException {
      String sql = "SELECT club_id FROM players WHERE id = ?::uuid";
      try{
          Connection conn = dataSource.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          stmt.setString(1, playerID);
          ResultSet rs = stmt.executeQuery();

          if(rs.next()){
              String clubId = rs.getString("club_id");
              conn.close();
              return clubId;
          }
      } catch (Exception e){
          e.getStackTrace();
          throw new RuntimeException(e);
      }
      return null;
    }

    //For stats
    public PlayerStat getOnePlayerStatForASeason(String playerID, String season) throws SQLException {
      String sql = "SELECT * FROM player_statistics WHERE player_id = ?::uuid AND season = ?";
      Connection conn = dataSource.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, playerID);
      stmt.setString(2, season);
      ResultSet rs = stmt.executeQuery();

        System.out.println("player id " + playerID + " season " + season);
        if (rs.next()) {
            System.out.println("ok");
            PlayerStat p =  new PlayerStat(
                rs.getString("player_id"),
                    rs.getString("season"),
                    rs.getInt("scored_goals"),
                    rs.getInt("playing_time"),
                    DurationUnit.MINUTE
            );
            conn.close();
            System.out.println(p.toString());
            return p;
        }
        System.out.println("noteok");
        return null;
    }

    public void updatePlayerStatForASeason(String playerID, String season, PlayerStat stat) throws SQLException {
      String sql = "UPDATE player_statistics SET scored_goals = ?, playing_time = ? WHERE player_id = ?::uuid AND season = ?";
      Connection conn = dataSource.getConnection();
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, stat.getScoredGoals());
      stmt.setInt(2, stat.getPlayingTime());
      stmt.setString(3, playerID);
      stmt.setString(4, season);
      stmt.executeUpdate();
    }

    public PlayerStatistic getPlayerStatisticForSeason(String playerId, String season) throws SQLException {
      String sql = "SELECT * FROM player_statistics WHERE player_id = ?::uuid AND season = ?";
      try{
          Connection conn = dataSource.getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql);
          stmt.setString(1, playerId);
          stmt.setString(2, season);
          ResultSet rs =  stmt.executeQuery();
          if(rs.next()){

            PlayerStatistic stat = new PlayerStatistic(
                    rs.getInt("scored_goals"),
                    new PlayingTime(
                            rs.getInt("playing_time"),
                            DurationUnit.valueOf(rs.getString("duration_unit"))
                    )
            );

            conn.close();

            return stat;
          }
          else {
              throw new SQLException("This stat does not exit");
          }} catch (Exception e) {
                e.getStackTrace();
              throw e;
          }

    }
}
