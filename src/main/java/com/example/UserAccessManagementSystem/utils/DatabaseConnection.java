package com.example.UserAccessManagementSystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/management";
    private static final String USER = "sandesh";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection(){
        try{
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Error while trying to connect to database");
        }
        catch(ClassNotFoundException e){
            System.out.println("Class not found");
            throw new RuntimeException(e);
        }
    }
}
