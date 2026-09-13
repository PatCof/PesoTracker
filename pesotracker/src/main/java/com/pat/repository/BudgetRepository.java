package com.pat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.pat.database.DatabaseConnection;

public class BudgetRepository {
    public void addBudget(String category, double amount, int user_id){
        Connection conn = DatabaseConnection.getConnection();

        var addBudgetStatement = "INSERT INTO budget (amount, category, date, user_id) VALUES (?,?,?,?);";
        String creationDate = LocalDate.now().toString();
      
        try{
            PreparedStatement stmt = conn.prepareStatement(addBudgetStatement);
            stmt.setDouble(1, amount);
            stmt.setString(2, category);
            stmt.setString(3, creationDate);
            stmt.setInt(4, user_id);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public void updateBudget(double amount, String category, int user_id, int id){
        Connection conn = DatabaseConnection.getConnection();
        var updateBudgetStatement = "UPDATE budget SET amount = ?, category = ? WHERE user_id = ? AND id = ?; ";
        try{
            PreparedStatement stmt = conn.prepareStatement(updateBudgetStatement);
            stmt.setDouble(1, amount);
            stmt.setString(2, category);
            stmt.setInt(3, user_id);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }


    public ResultSet selectAllBudget(int user_id){
        Connection conn = DatabaseConnection.getConnection();

        var selectBudgetStatement = "SELECT id, amount, category, date FROM budget WHERE user_id = ? GROUP BY category;";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(selectBudgetStatement);
            stmt.setInt(1, user_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs;
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ResultSet selectAllBudgetWithinRange(int user_id, LocalDate dateStart, LocalDate dateEnd){
        Connection conn = DatabaseConnection.getConnection();

        var selectBudgetStatement = "SELECT id, amount, category, date FROM budget WHERE user_id = ? AND date BETWEEN ? AND ? GROUP BY category ORDER BY category DESC;";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(selectBudgetStatement);
            stmt.setInt(1, user_id);
            stmt.setObject(2, dateStart);
            stmt.setObject(3, dateEnd);

            ResultSet rs = stmt.executeQuery();

            return rs;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }


    public Boolean checkCategoryExists(int user_id, String category){
        Connection conn = DatabaseConnection.getConnection();

        var checkBudgetStatement = "SELECT id FROM budget WHERE user_id = ? AND category = ?;";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(checkBudgetStatement);
            stmt.setInt(1, user_id);
            stmt.setString(2, category);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void deleteBudget(String category, int user_id){
        Connection conn = DatabaseConnection.getConnection();
        var deleteBudgetStatement = "DELETE FROM budget WHERE user_id = ? AND category = ?;";
        try{
            PreparedStatement stmt = conn.prepareStatement(deleteBudgetStatement);
            stmt.setInt(1,user_id);
            stmt.setString(2, category);
            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    
    }
}
