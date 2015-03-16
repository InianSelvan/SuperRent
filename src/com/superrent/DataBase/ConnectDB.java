/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.DataBase;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.superrent.configs.ReadProperties;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Iniyan
 */
public class ConnectDB extends ReadProperties{
    
   private static String jdbc_driver;
   private static String db_url;
   private static String db_name;
   private static String user;
   private static String pswd;
   private static Connection conn;
   private static Statement stmt;
   private static ResultSet rs;
   
    /**
     * Will connect to the specified database
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    
    public static void connect() throws IOException, ClassNotFoundException, SQLException{
        jdbc_driver = getPropValue("driver");
        db_url = getPropValue("connectionUrl");
        db_name = getPropValue("databaseName");
        user = getPropValue("userName");
        pswd = getPropValue("password");
                
        Class.forName(jdbc_driver);
        conn = (Connection) DriverManager.getConnection(db_url+db_name,user,pswd);
        stmt = (Statement) conn.createStatement();
    }
    
    public static void clearResultSet() throws SQLException{
        stmt.closeOnCompletion();
        rs.close();
        conn.close();       
    }
    
    /**
     * Run the query based on the SQL statement passed and returns the result set
     * @param SQL
     * @return Query result set
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException 
     */
    public static void exeQuery(String SQL) throws ClassNotFoundException, SQLException, IOException{
        connect();
        rs = stmt.executeQuery(SQL); 
    }
    
    public static void exeUpdate(String SQL) throws ClassNotFoundException, SQLException, IOException{
        connect();
        stmt.executeUpdate(SQL);
    }
    /**
     * After exeQuery the result set gets stored and will be returned when this method is called
     * @return ResultSet of he Statement
     */
    public static ResultSet resultSet(){
        return rs;
    }
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
        exeQuery("Select * from user");
        while(resultSet().next()){
            System.out.println(resultSet().getString(2));            
        }
        clearResultSet(); 
    }
}
