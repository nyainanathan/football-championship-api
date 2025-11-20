package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.mapper.PlayerRowMapper;
import com.nathan.localleagueapi.model.Club;
import com.nathan.localleagueapi.model.Coach;
import com.nathan.localleagueapi.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ClubRepo {

    private final DataSource dataSource;
    private final PlayerRowMapper playerRowMapper;

    public List<Club> getAllClubs(){
        String sql = "select clubs.*, coaches.* from clubs join coaches on clubs.coach_id = coaches.id";
        List<Club> clubs = new ArrayList<>();

        try{
            Connection conn =  dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){

                clubs.add(
                        new Club(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("acronym"),
                                rs.getInt("year_creation"),
                                rs.getString("stadium"),
                                new Coach(
                                        rs.getString(8),
                                        rs.getString(9)
                                )
                        )
                );
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
}

