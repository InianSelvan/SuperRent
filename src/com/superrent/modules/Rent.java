/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Iniyan
 */
public class Rent {
    
    public void fillBranchIDCombo(JComboBox branchCombo) throws ClassNotFoundException, SQLException, IOException{
        ConnectDB.exeQuery("SELECT Branch_ID FROM Branches");
        while(ConnectDB.resultSet().next()){
            int branchId = Integer.parseInt(ConnectDB.resultSet().getString("Branch_ID"));
            branchCombo.addItem(branchId);
        }
       ConnectDB.clearResultSet();
    } 
   
    public void fillCarTypeCombo(JComboBox cartypeCombo, JComboBox branchCombo) throws ClassNotFoundException, SQLException, IOException{
        int branchId = Integer.parseInt(branchCombo.getSelectedItem().toString());
        ConnectDB.exeQuery("SELECT Vehicle_Type FROM vehicles WHERE Branch_ID = "+branchId);
        while(ConnectDB.resultSet().next()){
            String carType = ConnectDB.resultSet().getString("Vehicle_Type");
            cartypeCombo.addItem(carType);
        }
        ConnectDB.clearResultSet();
    }
    
    public void fillEquipmentCombo(JComboBox equpType, JComboBox cartypeCombo) throws ClassNotFoundException, SQLException, IOException{
        String vehicalType = null;
        if(cartypeCombo.getSelectedItem()!=null)
        {
         vehicalType = cartypeCombo.getSelectedItem().toString();
        }
        
        ConnectDB.exeQuery("SELECT distinct(Equipment) FROM additional_equipment WHERE Vehicle_Type = '"+vehicalType+"'");
        while(ConnectDB.resultSet().next()){
            String EquipType = ConnectDB.resultSet().getString("Equipment");
            equpType.addItem(EquipType);
            
        }
        equpType.addItem("None");
        ConnectDB.clearResultSet();
    }
    
    public int validateCustomerInfo(JTextField cusNumField) throws ClassNotFoundException, SQLException, IOException{
        String phoneNum = cusNumField.getText();       
        ConnectDB.exeQuery("SELECT CustomerID FROM customer WHERE PhoneNumber ='"+phoneNum+"'");
        int cusID =0;
        if(ConnectDB.resultSet().next()){
            cusID = Integer.parseInt(ConnectDB.resultSet().getString("CustomerID"));
        }
        ConnectDB.clearResultSet();
        return cusID;
        
    }
        
    public static long genUniqueID(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH)+1;
        int date = now.get(Calendar.DATE);
        int hour = now.get(Calendar.HOUR);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        String random = ""+year+month+date+hour+min+sec;
        return Long.valueOf(random);
    }

    public  String[][] getAvailableVehicles(String vehicleType, int branchId) throws ClassNotFoundException, SQLException, IOException{
        int num = countAvailableVehicles(vehicleType, branchId);
        ConnectDB.exeQuery("select v.Vehicle_ID, vd.Make, v.Vehicle_Type, vd.Colour, vd.Doors from vehicles as v "
                + "inner join vehicle_details as vd on v.Vehicle_ID = vd.Vehicle_ID "
                + "where v.Availability = 1 and v.Vehicle_Type ='"+vehicleType+"' and v.Branch_ID ="+branchId);
        
        String[][] VehicleInfo= new String[num][5];
        for(int i=0; ConnectDB.resultSet().next(); i++){
            for(int j=1; j<5; j++){
                VehicleInfo[i][j-1]= ConnectDB.resultSet().getString(j);
            }
        }
        ConnectDB.clearResultSet();
        return VehicleInfo;       
    }
    
    public  int countAvailableVehicles(String vehicleType, int branchId) throws ClassNotFoundException, SQLException, IOException{
        int countVehi=0;
        ConnectDB.exeQuery("select Count(Vehicle_ID) from vehicles where Vehicle_Type = '"+vehicleType+"' and Branch_ID ="+branchId);
        while(ConnectDB.resultSet().next()){
            countVehi = Integer.parseInt(ConnectDB.resultSet().getString(1));
        }
        ConnectDB.clearResultSet();
        return countVehi;
    }
    

}
