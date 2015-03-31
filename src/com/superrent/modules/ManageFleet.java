/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JComboBox;

/**
 *
 * @author miaowu
 */
public class ManageFleet {
 public void fillCarType(JComboBox cartypeCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT category FROM fleet");
        while(ConnectDB.resultSet().next()){
            String cartype = ConnectDB.resultSet().getString("category");
            cartypeCombo.addItem(cartype);
        }
       ConnectDB.clearResultSet();
    }
 
  public void fillManufacturer(JComboBox manufacturerCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct maker FROM fleet");
        while(ConnectDB.resultSet().next()){
            String manufacturer = ConnectDB.resultSet().getString("maker");
            manufacturerCombo.addItem(manufacturer);
        }
       ConnectDB.clearResultSet();
    }
  
    public void fillName(JComboBox nameCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct maker FROM fleet");
        while(ConnectDB.resultSet().next()){
            String name = ConnectDB.resultSet().getString("maker");
            nameCombo.addItem(name);
        }
       ConnectDB.clearResultSet();
    }
    
    public void fillBranch(JComboBox branchidCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct branch_id FROM fleet");
        while(ConnectDB.resultSet().next()){
            int branchid = ConnectDB.resultSet().getInt("Branch_ID");
            branchidCombo.addItem(branchid);
        }
       ConnectDB.clearResultSet();
    }

      
       
    
    }                                           
 

