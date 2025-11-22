package com.nathan.localleagueapi.repository;

import com.nathan.localleagueapi.dto.CreateSeason;
import com.nathan.localleagueapi.model.season.Season;
import com.nathan.localleagueapi.model.Status;
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
public class SeasonRepo {

    private final DataSource dataSource;

    public List<Season> getAllExistingSeasons(){
        String sql = "SELECT * FROM seasons";

        List<Season> seasons = new ArrayList<>();

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Season s = new Season(
                    rs.getInt("year"),
                        rs.getString("alias"),
                        rs.getString("id"),
                        Status.valueOf(rs.getString("status"))
                );
                seasons.add(s);
            }

            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return seasons;
    }

    public List<Season> createSeasons(List<CreateSeason> seasons){

        List<Season> everySeasons;

        try{
            Connection conn = dataSource.getConnection();
            String insertSql = "INSERT INTO seasons (year, alias)  VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            for(CreateSeason season : seasons){
                stmt.setInt(1, season.getYear());
                stmt.setString(2, season.getAlias());
                stmt.execute();
            }
            conn.close();
            everySeasons = this.getAllExistingSeasons();
            return everySeasons;

        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public Season getSeasonByYear(String seasonYear){
        String sql = "SELECT * FROM  seasons WHERE year = ?";
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(seasonYear));
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Season(
                        rs.getInt("year"),
                        rs.getString("alias"),
                        rs.getString("id"),
                        Status.valueOf(rs.getString("status"))
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Season updateSeasonStatus(String seasonYear, Status newStatus){
        String setSql = "UPDATE seasons SET status=?::status_enum WHERE year=?";
        String statusString = newStatus.name();
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(setSql);
            stmt.setString(1, statusString);
            stmt.setInt(2, Integer.parseInt(seasonYear));
            stmt.execute();
            conn.close();
            return this.getSeasonByYear(seasonYear);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}