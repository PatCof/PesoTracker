package com.pat.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.pat.database.DatabaseConnection;

public class UserRepository {
    private int userId;


    public void addUser(String username, String password){
        Connection conn = DatabaseConnection.getConnection();
        var addUserStatement = "INSERT INTO users (username, password, creation_date) VALUES (?,?,?);";
        String creationDate =  LocalDate.now().toString();

        try{
            PreparedStatement stmt = conn.prepareStatement(addUserStatement);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, creationDate);

            stmt.executeUpdate();
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public int checkUser(String username){
        Connection conn = DatabaseConnection.getConnection();
        var checkUserStatement = "SELECT user_id FROM users WHERE username = ? LIMIT 1;";
        try{
            PreparedStatement stmt = conn.prepareStatement(checkUserStatement);
            stmt.setString(1,username);

            ResultSet userValue = stmt.executeQuery();

            if(userValue.next()){
               userId = userValue.getInt("user_id");
            }else{
                userId = -1;
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return userId;
    }

    public int selectUser(String username, String password){
        Connection conn = DatabaseConnection.getConnection();
        
        var selectUserStatement = "SELECT user_id, password FROM users WHERE username = ?;";

        try{
            PreparedStatement stmt = conn.prepareStatement(selectUserStatement);
            stmt.setString(1,username);
            //stmt.setString(2, password);
            
            ResultSet userValue = stmt.executeQuery();

            if(userValue.next()){
                if(password.equals(userValue.getString("password"))){
                    return -2;
                }
                userId = userValue.getInt("user_id");
            }else{
                userId = -1;
            }
            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return userId;
    }
}
