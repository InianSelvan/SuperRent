/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander
 */
public class Customer {
    
    // PUBLIC FUNCTIONS
    
    // TEST FUNCTIONS
    
    public boolean testPhone(String phone) throws SQLException, ClassNotFoundException, IOException{
      boolean pass = true;
      try{  
        ConnectDB.exeQuery("SELECT phone FROM customer WHERE phone ='"+phone+"'");
        if(ConnectDB.resultSet().next())
          pass = false;
        ConnectDB.clearResultSet();
      }catch(SQLException | ClassNotFoundException | IOException ex){
     }
      return pass;
    }
    
    public boolean testName(String name){
      boolean pass = true;
      if(name.matches(".*\\d+.*"))
        pass = false;
      return pass;
    }    
    

    public boolean testZip(String zip){
      String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(zip);
      return matcher.matches();
    }
    
}
