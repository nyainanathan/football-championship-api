package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.model.Club;
import com.nathan.localleagueapi.model.Coach;
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

    public List<Club> getAllClubs(){
        String sql = "select * from clubs";
        List<Club> clubs = new ArrayList<>();
        String coachSql = "SELECT * FROM coaches WHERE id = ?::uuid";
        try{
            Connection conn =  dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                UUID coachId = UUID.fromString(rs.getString("coach_id"));
                PreparedStatement stmt2 = conn.prepareStatement(coachSql);
                stmt2.setString(1, String.valueOf(coachId));
                ResultSet rs2 = stmt2.executeQuery();
                String coachName;
                String coachNationality;
                Coach coach = null;
                if(rs2.next()){
                        coach = new Coach(
                                rs2.getString("name"),
                                rs2.getString("nationality")
                        );
                }
                clubs.add(
                        new Club(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("acronym"),
                                rs.getInt("year_creation"),
                                rs.getString("stadium"),
                                coach
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
}

