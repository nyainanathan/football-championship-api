package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.dto.ClubStat;
import com.nathan.localleagueapi.dto.MatchFilter;
import com.nathan.localleagueapi.dto.MatchRawData;
import com.nathan.localleagueapi.mapper.ClubMinimalInfoRowMapper;
import com.nathan.localleagueapi.mapper.ClubRowMapper;
import com.nathan.localleagueapi.mapper.ClubStatRowMapper;
import com.nathan.localleagueapi.mapper.PlayerRowMapper;
import com.nathan.localleagueapi.model.Status;
import com.nathan.localleagueapi.model.club.Club;
import com.nathan.localleagueapi.model.club.ClubMinimumInfo;
import com.nathan.localleagueapi.model.club.ClubStatics;
import com.nathan.localleagueapi.model.club.Coach;
import com.nathan.localleagueapi.model.match.Match;
import com.nathan.localleagueapi.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@AllArgsConstructor
public class ClubRepo {

    private final DataSource dataSource;
    private final PlayerRowMapper playerRowMapper;
    private final ClubStatRowMapper  clubStatRowMapper;
    private final ClubRowMapper clubRowMapper;
    private final ClubMinimalInfoRowMapper clubMinimalInfoRowMapper;


    public List<Club> getAllClubs(){
        String sql = "select clubs.*, coaches.* from clubs join coaches on clubs.coach_id = coaches.id";
        List<Club> clubs = new ArrayList<>();

        try{
            Connection conn =  dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){

                clubs.add(clubRowMapper.map(rs));
            }

            return clubs;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Club getClubById(UUID id){

        String sql = "SELECT club.*, coach.* FROM clubs AS club INNER JOIN coaches AS coach ON club.coach_id = coach.id WHERE club.id = ?::uuid";

        try{
            Connection conn =  dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return clubRowMapper.map(rs);
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public Club updateClub(Club updatedClub, UUID coachId) throws Exception {
        String sql = "UPDATE clubs SET name= ?, acronym= ? , year_creation= ? , stadium= ? , coach_id = ?::uuid WHERE id = ?::uuid RETURNING *";

        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, updatedClub.getName());
            stmt.setString(2, updatedClub.getAcronym());
            stmt.setInt(3, updatedClub.getYearCreation());
            stmt.setString(4, updatedClub.getStadium());
            stmt.setString(5, String.valueOf(coachId));
            stmt.setString(6, String.valueOf(updatedClub.getId()));
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return new Club(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("acronym"),
                        rs.getInt("year_creation"),
                        rs.getString("stadium"),
                        updatedClub.getCoach()
                );
            }

        } catch (Exception e){
            throw new Exception(e);
        }
        return null;
    }

    public Club createClub(Club newClub, UUID coachId) throws Exception {
        String sql = "INSERT INTO clubs (name, acronym, year_creation, stadium, coach_id) VALUES (?, ?, ?, ?, ?::uuid)";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newClub.getName());
            stmt.setString(2, newClub.getAcronym());
            stmt.setInt(3, newClub.getYearCreation());
            stmt.setString(4, newClub.getStadium());
            stmt.setString(5, String.valueOf(coachId));
            stmt.executeUpdate();

            return newClub;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public List<Player> getClubPlayer(String clubId){
        String sql = "SELECT * FROM players  WHERE club_id = ?::uuid";
        List<Player> players = new  ArrayList<>();
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, clubId);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                players.add(playerRowMapper.map(rs));
            }
            conn.close();
            return players;
        } catch (Exception e){
            e.getStackTrace();
            throw new RuntimeException(e);
        }

    }

    public Player removePlayerFromClub(String playerId){
        String sql = "UPDATE players SET club_id = NULL where id = ?::UUID RETURNING *";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,playerId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Player player = playerRowMapper.map(rs);
                conn.close();
                return player;
            }
        } catch(Exception e){
            e.getStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    public Player attachPlayerToClub(String clubId, String playerId){
        String sql = "UPDATE players SET club_id = ?::uuid where id = ?::UUID RETURNING *";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, clubId);
            stmt.setString(2,playerId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                System.out.println("there is a result set");
                return playerRowMapper.map(rs);
            }
        } catch(Exception e){
            e.getStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    public ClubMinimumInfo getOneClubMinimumInfo(String clubId){
        String sql = "SELECT id, name, acronym FROM clubs WHERE id = ?::uuid";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, clubId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                ClubMinimumInfo info =  clubMinimalInfoRowMapper.map(rs);
                conn.close();
                return info;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<ClubMinimumInfo> getAllClubsMinimumInfo(){
        String sql = "SELECT id, name, acronym FROM clubs";
        try{
            List<ClubMinimumInfo> clubs = new  ArrayList<>();
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                clubs.add(clubMinimalInfoRowMapper.map(rs));
            }
            conn.close();
            return clubs;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    //Stats

    public ClubStat getOneClubStatics(String clubId) throws SQLException{
        String sql = "SELECT * FROM clubs_statistics WHERE id = ?::uuid";
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, clubId);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return new ClubStat(
                rs.getString("id"),
                    rs.getInt("ranking_point"),
                    rs.getInt("scored_goals"),
                    rs.getInt("conceded_goals"),
                    rs.getInt("difference_goals"),
                    rs.getInt("clean_sheets"),
                    rs.getString("season")
            );
        }
        return null;
    }

    

    public List<ClubStatics> getAllClubsStats(boolean hasToBeClassified, String season) throws SQLException {
        StringBuilder sql = new StringBuilder("select c.*, s.*, co.name as coach_name, co.nationality as coach_nationality from clubs as c inner join clubs_statistics as s on c.id = s.id inner join coaches as co on c.coach_id = co.id where season = ? order by ");
        if(hasToBeClassified){
            sql.append("s.ranking_point desc");
        } else {
            sql.append("c.name asc");
        }
        String finalSql = sql.toString();
        try{
            List<ClubStatics> stats = new   ArrayList<>();
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(finalSql);
            stmt.setString(1, season);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                stats.add(clubStatRowMapper.map(rs));
            }
            conn.close();
            return hasToBeClassified ?  stats.stream()
                    .sorted(
                            Comparator.comparing(ClubStatics::getRankingPoint)
                                    .thenComparing(ClubStatics::getDifferenceGoals)
                                    .thenComparing(ClubStatics::getCleanSheetNumber)
                                    .reversed()
                    )
                    .toList() : stats;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public void updateStats(ClubStat stat) throws SQLException{
        String sql = "update clubs_statistics set ranking_point = ? , scored_goals = ? , conceded_goals = ? , clean_sheets = ? where id = ?:uuid";
        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, stat.getRankingPoint());
        stmt.setInt(2, stat.getScoredGals());
        stmt.setInt(3, stat.getConcededGoals());
        stmt.setInt(4, stat.getCleanSheets());
        stmt.setString(5, stat.getId());
        stmt.executeUpdate();
    }
}

