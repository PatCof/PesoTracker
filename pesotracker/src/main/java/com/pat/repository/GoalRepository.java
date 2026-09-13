package com.pat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pat.database.DatabaseConnection;

public class GoalRepository {
    public ResultSet getGoal(int user_id){
        Connection conn = DatabaseConnection.getConnection();
        var getGoalStatement = "SELECT id, savings, end, category FROM goals WHERE user_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(getGoalStatement);
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
        
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null; 
    }

    public void setGoal(double savings, double end, String category, int user_id){
        Connection conn = DatabaseConnection.getConnection();
        var setGoalStatement = "INSERT INTO goals (savings, end, category, user_id) VALUES (?,?,?,?);";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(setGoalStatement);
            stmt.setDouble(1, savings);
            stmt.setDouble(2, end);
            stmt.setString(3, category);
            stmt.setInt(4, user_id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());    
        }    
    }

    public void deleteGoal(int id, int user_id){
        Connection conn = DatabaseConnection.getConnection();
        var deleteStatement = "DELETE FROM goals WHERE id = ? AND user_id = ?";
        try{
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setInt(1, id);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public void updateGoalInDB(int user_id, int id, String category, double savings, double end){
        Connection conn = DatabaseConnection.getConnection();
        var updateStatement = "UPDATE goals SET category = ?, savings = ?, end = ? WHERE user_id = ? AND id= ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(updateStatement);
            stmt.setString(1, category);
            stmt.setDouble(2, savings);
            stmt.setDouble(3, end);
            stmt.setInt(4, user_id);
            stmt.setInt(5, id);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
