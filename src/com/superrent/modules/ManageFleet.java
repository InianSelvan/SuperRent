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
    int car_or_truck;
    
 public void getCar_or_truck(int car_or_truck){
     
     //car is 0 and truck is 1
     this.car_or_truck = car_or_truck;
 }
 public void fillCarType(JComboBox cartypeCombo){
       String [] carTypeArray = { "Economy","Compact", "Mid-size", "Standard", "Full-size", "Premium", "Luxury", "SUV","Van"};
       String [] truckTypeArray = {"24-foot", "15-foot", "12-foot", "Box-Trucks","Cargo-Vans"};
        int carType_no;
        if(car_or_truck == 0)
        for(carType_no = 0 ; carType_no<9 ; carType_no++){
            cartypeCombo.addItem(carTypeArray[carType_no]); 
        }
        
        else if(car_or_truck ==1)
            for(carType_no = 0 ; carType_no<5 ; carType_no++){
            cartypeCombo.addItem(truckTypeArray[carType_no]);
        }
                
    }
 
//  public void fillManufacturer(JComboBox manufacturerCombo) throws ClassNotFoundException, SQLException, IOException{
//        ConnectDB.exeQuery("SELECT distinct maker FROM fleet");
//        while(ConnectDB.resultSet().next()){
//            String manufacturer = ConnectDB.resultSet().getString("maker");
//            manufacturerCombo.addItem(manufacturer);
//        }
//       ConnectDB.clearResultSet();
//    }
  
    public void fillManufacturer(JComboBox modelCombo){
        String [] modelArray = {"Maserati","Honda","Infinity","BMW","Benz","Acura","GMC","Toyota","other"};
        int model_no;
        for(model_no = 0;model_no<9;model_no++){
            modelCombo.addItem(modelArray[model_no]);
        }
      
    }
    
    public void fillBranch(JComboBox branchidCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT distinct branch_id FROM branch");
        while(ConnectDB.resultSet().next()){
            int branchid = ConnectDB.resultSet().getInt("branch_id");
            branchidCombo.addItem(branchid);
        }
       ConnectDB.clearResultSet();
    }
    
        public void fillColor(JComboBox colorCombo){
        String [] colorArray = {"black","blue","brown","grey","red","orange","silver","white","yellow","other"};
        int color_no;
        for(color_no = 0;color_no<10;color_no++){
            colorCombo.addItem(colorArray[color_no]);
        }

    }

      
       
    
    }                                           
 

