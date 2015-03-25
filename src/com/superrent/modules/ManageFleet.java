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
        ConnectDB.exeQuery("SELECT Vehicle_Type FROM vehicles");
        while(ConnectDB.resultSet().next()){
            String cartype = ConnectDB.resultSet().getString("Vehicle_Type");
            cartypeCombo.addItem(cartype);
        }
       ConnectDB.clearResultSet();
    }
 
  public void fillManufacturer(JComboBox manufacturerCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct Manufacturer FROM vehicles");
        while(ConnectDB.resultSet().next()){
            String manufacturer = ConnectDB.resultSet().getString("Manufacturer");
            manufacturerCombo.addItem(manufacturer);
        }
       ConnectDB.clearResultSet();
    }
  
    public void fillName(JComboBox nameCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct Name FROM vehicles");
        while(ConnectDB.resultSet().next()){
            String name = ConnectDB.resultSet().getString("Name");
            nameCombo.addItem(name);
        }
       ConnectDB.clearResultSet();
    }
    
    public void fillBranch(JComboBox branchidCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct Branch_ID FROM vehicles");
        while(ConnectDB.resultSet().next()){
            int branchid = ConnectDB.resultSet().getInt("Branch_ID");
            branchidCombo.addItem(branchid);
        }
       ConnectDB.clearResultSet();
    }

      
       
    
    }                                           
 

