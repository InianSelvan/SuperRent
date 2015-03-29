/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import com.superrent.gui.SuperRent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Period;

/**
 *
 * @author Iniyan
 */
public class Return {
    
    public int getNumberofWeeks(Date pickupdt, Date returndt){
        int weeks = 0;
        if(getNumberofhours(pickupdt, returndt)%7>1){
            weeks = getNumberofhours(pickupdt, returndt)%7;            
        }
        return weeks;
    }
    
    public int getNumberofDays(Date pickupdt, Date returndt){
        int days = 0;
        if(getNumberofhours(pickupdt, returndt)%24>1){
            days = getNumberofhours(pickupdt, returndt)%24;            
        }
        return days;
    }
    
    public int getNumberofhours(Date pickupdt, Date returndt){
        Calendar pickup = Calendar.getInstance();
        Calendar dropoff = Calendar.getInstance();
        pickup.setTime(pickupdt);
        dropoff.setTime(returndt);        
        DateTime start = new DateTime(pickup.get(Calendar.YEAR), pickup.get(Calendar.MONTH)+1, pickup.get(Calendar.DATE), pickup.get(Calendar.HOUR), pickup.get(Calendar.MINUTE), 0, 0);
        DateTime end = new DateTime(dropoff.get(Calendar.YEAR), dropoff.get(Calendar.MONTH)+1, dropoff.get(Calendar.DATE), dropoff.get(Calendar.HOUR), dropoff.get(Calendar.MINUTE), 0, 0);
        Hours hours = Hours.hoursBetween(start, end);
        return hours.getHours();
    }
    
//    public Date getpickupDate(long phoneNum){
//        return 
//    }
//    
   
    
    public String[][] getResrInfo(String vin){
          String[][] ReserInfo = new String [1][5];
        try {
       //int  IntFleetID = Integer.parseInt(vin);
        ConnectDB.exeQuery("select confirmation_no, firstname, lastname, vin, status from reserve where vin ='" +vin+ "'");
        
        if (ConnectDB.resultSet().next()){ 
        for (int j=1; j<=5 ; j++)
        {
            ReserInfo[0][j-1] = ConnectDB.resultSet().getString(j);
           System.out.println(ConnectDB.resultSet().getString(j));
        }
        }
        ConnectDB.clearResultSet();
        
          }catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
                }

        return ReserInfo;
    }
    
    public String[] getResrTime(String vin){
        String[] ReserTime = new String [2];
        try{
            ConnectDB.exeQuery("select  pickup_time, dropoff_time from reserve where vin = '"+vin+"'");
            if (ConnectDB.resultSet().next()){ 
                for (int j=1; j<=2 ; j++)
                {
                    ReserTime[j-1] = ConnectDB.resultSet().getString(j);
                    //System.out.println(ReserInfoDB.resultSet().getString(j+1));
                }
          }
          ConnectDB.clearResultSet();
        }catch(ClassNotFoundException | SQLException | IOException ex){
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ReserTime;
    }
    
        public String[][] getOverDue(){
          String[][] OverDueInfo ;
            int num_OverDue = 0;
        
            try {
            // count how many over due
                    ConnectDB.exeQuery("SELECT COUNT(*) FROM reserve where dropoff_time <= Now() and status = 'rented' ");
                     if (ConnectDB.resultSet().next()){ 
                          num_OverDue = Integer.parseInt(ConnectDB.resultSet().getString(1));
                        
                  }else{
                         JOptionPane.showMessageDialog(null, "There is no overdue.");
                         return null;
                     }
             ConnectDB.clearResultSet();
                } catch (ClassNotFoundException | SQLException | IOException ex){
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            OverDueInfo = new String[num_OverDue][7] ;
            
           try{ 
            ConnectDB.exeQuery("select confirmation_no,firstname, lastname, phone, branch_id, "
                    + "dropoff_time, vin from reserve where dropoff_time <= Now() and status = 'rented' ");
            int j = 1;
            
                for (int i = 0; i < num_OverDue; i++) {
                       if (ConnectDB.resultSet().next()){
                        for (int k = 0; k < 7; k++) { 
                        OverDueInfo[i][k] = ConnectDB.resultSet().getString(k+1);
                        }
                    }
                }
          ConnectDB.clearResultSet();
        }catch(ClassNotFoundException | SQLException | IOException ex){
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return OverDueInfo;
    }
}     

