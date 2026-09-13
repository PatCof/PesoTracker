package com.pat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.pat.database.DatabaseConnection;

public class ExpenseRepository {
    public ResultSet getExpenseValue(int user_id, LocalDate start, LocalDate end){
        Connection conn = DatabaseConnection.getConnection();
        var getExpenseStatement = "SELECT expense.amount AS expenseAmount, budget.category AS budgetCategory, budget.amount AS budgetAmount, expense.date AS expenseDate,  expense.id AS expenseId FROM expense " 
        + "RIGHT JOIN budget ON budget.category = expense.category AND budget.user_id = expense.user_id "
        + "WHERE budget.user_id = ? AND budget.date BETWEEN ? and ? GROUP BY expense.category ORDER BY budget.amount DESC;";

        try {
            PreparedStatement stmt = conn.prepareStatement(getExpenseStatement);
            stmt.setInt(1, user_id);
            stmt.setString(2, start.toString());
            stmt.setString(3, end.toString());

            ResultSet rs = stmt.executeQuery();
            
            return rs;

        } catch (SQLException e) {
            System.err.println(e.getMessage());    
        }
        return null;
    }



    public void setExpenseValue(double amount, int user_id, String category){
        Connection conn = DatabaseConnection.getConnection();
        
        var setExpenseStatement = "INSERT INTO expense (amount, date, user_id, category) VALUES (?,?,?,?);";
        String creationDate = LocalDate.now().toString();

        try{
            PreparedStatement stmt = conn.prepareStatement(setExpenseStatement);
            stmt.setDouble(1, amount);
            stmt.setString(2, creationDate);
            stmt.setInt(3, user_id);
            stmt.setString(4, category);

            stmt.executeUpdate();

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public void updateExpense(double amount, int user_id, String category, int valueId){
        Connection conn = DatabaseConnection.getConnection();
        var updateExpenseStatement = "UPDATE expense SET amount = ? WHERE category = ? AND user_id = ? AND id = ?;";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(updateExpenseStatement);
            stmt.setDouble(1, amount);
            stmt.setString(2, category);
            stmt.setInt(3, user_id);
            stmt.setInt(4, valueId);
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addExpense(double amount, int user_id, String category){
        Connection conn = DatabaseConnection.getConnection();
        var addExpenseStatement = "INSERT INTO expense (amount, date, category, user_id) VALUES(?,?,?,?);";
        String creationDate = LocalDate.now().toString();

        try {
            PreparedStatement stmt = conn.prepareStatement(addExpenseStatement);
            stmt.setDouble(1, amount);
            stmt.setString(2, creationDate);
            stmt.setString(3, category);
            stmt.setInt(4, user_id);
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public ResultSet getTotalBudget(int user_id, LocalDate start, LocalDate end){
        Connection conn = DatabaseConnection.getConnection();
        var getTotalBudgetStatement = "SELECT amount FROM budget WHERE user_id = ? AND date BETWEEN ? AND ?;";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(getTotalBudgetStatement);
            stmt.setInt(1, user_id);
            stmt.setString(2, start.toString());
            stmt.setString(3, end.toString());

            ResultSet rs = stmt.executeQuery();

            return rs;
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        
        return null;
    }
}
