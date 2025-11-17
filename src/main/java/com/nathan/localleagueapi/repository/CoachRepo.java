package com.nathan.localleagueapi.repository;


import com.nathan.localleagueapi.model.Coach;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@AllArgsConstructor
public class CoachRepo {

    private final DataSource dataSource;

    public Coach getCoachByName(String coachName) throws Exception {
        String sql = "select * from coaches where name = ?";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return new Coach(
                        rs.getString("name"),
                        rs.getString("nationality")
                );
            }

            conn.close();
        } catch (Exception e){
            throw new Exception(e);
        }
        return null;
    }

    public String getCoachIdByName(String coachName) throws Exception {
        String sql = "select id from coaches where name = ?";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getString("id");
            }

            conn.close();
        } catch (Exception e){
            throw new Exception(e);
        }
        return null;
    }
}
