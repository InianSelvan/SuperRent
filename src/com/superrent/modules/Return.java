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
import java.text.ParseException;
import static java.time.LocalDate.now;
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
    //Yaoyao modified
    public int getNumberofWeeks(Date pickupdt, Date returndt){
        int weeks = 0;
        if(getNumberofDays(pickupdt, returndt)/7>1){
            weeks = getNumberofDays(pickupdt, returndt)/7;            
        }
        return weeks;
    }
//        public int getNumberofWeeks(Date pickupdt, Date returndt){
//        int weeks = 0;
//        if(getNumberofhours(pickupdt, returndt)%7>1){
//            weeks = getNumberofhours(pickupdt, returndt)%7;            
//        }
//        return weeks;
//    }
    
    public int getNumberofDays(Date pickupdt, Date returndt){
        int days = 0;
        if(getNumberofhours(pickupdt, returndt)/24>1){
            days = getNumberofhours(pickupdt, returndt)/24;            
        }
        return days;
    }
//        public int getNumberofDays(Date pickupdt, Date returndt){
//        int days = 0;
//        if(getNumberofhours(pickupdt, returndt)%24>1){
//            days = getNumberofhours(pickupdt, returndt)%24;            
//        }
//        return days;
//    }
    
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
            ConnectDB.exeQuery("select pickup_time, dropoff_time from reserve where vin = '"+vin+"'");
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
//        
//    public String[] getVechileRate(String Vin){
//        String[] VechileRate = new String[8];
//        
//        try {
//        ConnectDB.exeQuery("select ");
//            
//        } catch (Exception e) {
//        }
//    }
    
     public String[][] getCalculateFee(String Vin, boolean roadStar, String Fuel, String Distance, boolean Reedem, Date Drop) throws ParseException{
         
        if (!Vin.isEmpty()){
                String[] ResrTime =getResrTime(Vin);
                String Pickup_Date = ResrTime[0];
                String DropOff_Date_string = ResrTime[1];
                
                Date Pickup_Date_Date = CommonFunc.StringToDate(Pickup_Date);
                Date DropOff_Date_Date = CommonFunc.StringToDate(DropOff_Date_string);
                
                if(DropOff_Date_Date.before(Drop)){
                    DropOff_Date_Date = Drop;
                }
        int num_weeks = getNumberofWeeks(Pickup_Date_Date, DropOff_Date_Date);
        int num_days = getNumberofDays(Pickup_Date_Date, DropOff_Date_Date) - 7*num_weeks;
        int num_hours = getNumberofhours(Pickup_Date_Date, DropOff_Date_Date) - 24*num_days - 24*7*num_weeks;

        String[] FeeRate = new String[6];
        FeeRate = getFeeRate(Vin);
        String FuelRate = getFuelRate(Vin);
        
        String[][] DisplayFee = new String[11][3];
        
        DisplayFee[0][0] = "Normal:";
        DisplayFee[0][1] = "Weeks: "+ Double.parseDouble(FeeRate[0]) + " x "+ num_weeks + 
                ". Days: "+ Double.parseDouble(FeeRate[1])+ " x "+ num_days + ". Hours: "+ Double.parseDouble(FeeRate[2]) + " x "+ num_hours;
        DisplayFee[0][2] = ""+((Double.parseDouble(FeeRate[0]) * num_weeks)+(Double.parseDouble(FeeRate[1]) * num_days) + (Double.parseDouble(FeeRate[2]) * num_hours));
        
        //DisplayFee[1][0] = "Over Due:";
        
        DisplayFee[2][0] = "Insurance:";
        DisplayFee[2][1] = "Weeks: "+ Double.parseDouble(FeeRate[3]) + " x "+ num_weeks + 
                ". Days: "+ Double.parseDouble(FeeRate[4]) + " x "+ num_days + ". Hours: "+ Double.parseDouble(FeeRate[5]) + " x "+ num_hours;
        DisplayFee[2][2] = ""+((Double.parseDouble(FeeRate[3]) * num_weeks)+(Double.parseDouble(FeeRate[4]) * num_days) + (Double.parseDouble(FeeRate[5]) * num_hours));
        
        DisplayFee[4][0] = "Equipment:";
        
        DisplayFee[6][0] = "Fuel: ";
        DisplayFee[6][1] = FuelRate+" x " + Fuel+".";
        DisplayFee[6][2] = ""+Double.parseDouble(FuelRate)* Double.parseDouble(Fuel);
        
        DisplayFee[8][0] = "Distance: ";
        
        DisplayFee[10][0] = "Membership Redeem:";
        
        return DisplayFee;
        }else{
                JOptionPane.showMessageDialog(null, "Please input the Vin.");
        }
        
     return null;
     }    
     
     
     
    public String getFuelRate(String Vin){
         String FuelRate = "0";
         try{
             ConnectDB.exeQuery("select G.gas_rate from fleet as FL, feature as FE, gas as G "
                     + "where FL.vin = '"+Vin+"' and FL.category = FE.category and FE.gas_no = G.gas_no");
             if(ConnectDB.resultSet().next()){
                 FuelRate = ConnectDB.resultSet().getString(1);
             }
             ConnectDB.clearResultSet();
         }catch(ClassNotFoundException | SQLException | IOException ex){
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
         return FuelRate;
     }
    
    //Get all fee rate of a category, which is stored in the String 
    public String[] getFeeRate(String Vin){
        String[] FeeRate =  new String[7]; 
        try{
             ConnectDB.exeQuery("select FE.weekly_rates, FE.daily_rates, FE.hourly_rates, FE.weekly_insurance, FE.daily_insurance, "
                     + "FE.hourly_insurance, FE.perkm, G.gas_rate "
                     + "from fleet as FL, feature as FE "
                     + "where FL.vin = '"+Vin+"' and FL.category = FE.category");
             if(ConnectDB.resultSet().next()){
                 for(int i = 0; i < 7; i++){
                 FeeRate[i] = ConnectDB.resultSet().getString(i+1);
                 }
             }
             ConnectDB.clearResultSet();
         }catch(ClassNotFoundException | SQLException | IOException ex){
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FeeRate;
    }
    
//    public String getExtendedDistance(String Vin, String OdeReading) {
//        
//        
//    }
     
}     

