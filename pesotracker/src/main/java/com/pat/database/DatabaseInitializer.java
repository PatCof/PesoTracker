package com.pat.database;

import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initializer(){

        var conn = DatabaseConnection.getConnection();
        if(conn != null){
            try{
                var usersTable = "CREATE TABLE IF NOT EXISTS users("
                + "user_id INTEGER PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "creation_date TEXT NOT NULL"
                + ");";

                var budgetTable = "CREATE TABLE IF NOT EXISTS budget("
                + "id INTEGER PRIMARY KEY,"
                + "amount REAL NOT NULL,"
                + "category TEXT NOT NULL,"
                + "date TEXT NOT NULL,"
                + "user_id INTEGER REFERENCES users(user_id) "
                + "ON DELETE CASCADE "
                + "ON UPDATE CASCADE "
                + ");";

                var expenseTable = "CREATE TABLE IF NOT EXISTS expense("
                + "id INTEGER PRIMARY KEY,"
                + "amount REAL,"
                + "date TEXT NOT NULL,"
                + "category TEXT,  "
                + "user_id INTEGER REFERENCES users(user_id) "
                + "ON DELETE CASCADE "
                + "ON UPDATE CASCADE "
                + ");";

                var goalsTable = "CREATE TABLE IF NOT EXISTS goals("
                + "id INTEGER PRIMARY KEY, "
                + "savings REAL, "
                + "end REAL, "
                + "category TEXT, "
                + "user_id INTEGER REFERENCES users(user_id) "
                + "ON DELETE CASCADE "
                + "ON UPDATE CASCADE "
                + ");";

                var stmt = conn.createStatement();
                stmt.execute(usersTable);
                stmt.execute(budgetTable);
                stmt.execute(expenseTable);
                stmt.execute(goalsTable);

            }catch(SQLException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
