package com.pat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {
    public static Connection conn;
    public static final String URL = "jdbc:sqlite:pesotracker/database.db";
    
    public static void connect(){
        try{
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys= ON;");
            System.out.println("Connection to SQLITE has been established");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void close(){
        try{
            if(conn != null){
                conn.close();
            }            
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
