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
    
    public static String[][] getResrInfo(String vin){
          String[][] ReserInfo = new String [1][7];
        try {
       //int  IntFleetID = Integer.parseInt(vin);
        ConnectDB.exeQuery("select confirmation_no, customer_id, vin, status from reserve where vin ='" +vin+ "'"
                + " and status = 'rented'");
        
        if (ConnectDB.resultSet().next()){ 
        for (int j=1; j<=4 ; j++)
        {
            ReserInfo[0][j-1] = ConnectDB.resultSet().getString(j);
           System.out.println(ConnectDB.resultSet().getString(j));
        }
        }
        ConnectDB.clearResultSet();
        
        ConnectDB.exeQuery("select firstname, lastname, phone from customer where customer_id = '"+ReserInfo[0][1]+"'");
        if(ConnectDB.resultSet().next()){
            for(int i =1; i<4 ; i++){
                ReserInfo[0][i+3] = ConnectDB.resultSet().getString(i);
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
    
    public String[][] getOverDue() {
        String[][] OverDueInfo;
        int num_OverDue = 0;

        try {
            // count how many over due
            ConnectDB.exeQuery("SELECT COUNT(*) FROM reserve where dropoff_time <= Now() and status = 'rented' ");
            if (ConnectDB.resultSet().next()) {
                num_OverDue = Integer.parseInt(ConnectDB.resultSet().getString(1));

            } else {
                JOptionPane.showMessageDialog(null, "There is no overdue.");
                return null;
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }

        OverDueInfo = new String[num_OverDue][6];

        try {
            ConnectDB.exeQuery("select confirmation_no, customer_id, phone, branch_id, "
                    + "dropoff_time, vin from reserve where dropoff_time <= Now() and status = 'rented' ");
            int j = 1;

            for (int i = 0; i < num_OverDue; i++) {
                if (ConnectDB.resultSet().next()) {
                    for (int k = 0; k < 6; k++) {
                        OverDueInfo[i][k] = ConnectDB.resultSet().getString(k + 1);
                    }
                }
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return OverDueInfo;
    }

    public String getEstimateFee(String Vin, Date drop){
        String totalFee = "";
   
        try {
            totalFee = getCalculateFee(null, false, "0", "0", false, drop)[14][2];
        } catch (ParseException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
    return  totalFee;
    }
    
    public String[][] getCalculateFee(String Vin, boolean roadStar, String Fuel, String Distance, boolean Reedem, Date Drop) throws ParseException {

        if (!Vin.isEmpty()) {
            double totalFee = 0;
            double InsuranceFee = 0;
            double tempFee = 0;
            
            String[] ResrTime = getResrTime(Vin);
            String Pickup_Date = ResrTime[0];
            String DropOff_Date_string = ResrTime[1];

            Date Pickup_Date_Date = CommonFunc.StringToDate(Pickup_Date);
            Date DropOff_Date_Date = CommonFunc.StringToDate(DropOff_Date_string);
            int NumHour_OverDue = 0;

            if (DropOff_Date_Date.before(Drop)) {
                NumHour_OverDue = getNumberofhours(Pickup_Date_Date, Drop) - getNumberofhours(Pickup_Date_Date, DropOff_Date_Date);
                //DropOff_Date_Date = Drop;
            } else if (Drop.before(DropOff_Date_Date)) {
                DropOff_Date_Date = Drop;
            }
            int num_weeks = getNumberofWeeks(Pickup_Date_Date, DropOff_Date_Date);
            int num_days = getNumberofDays(Pickup_Date_Date, DropOff_Date_Date) - 7 * num_weeks;
            int num_hours = getNumberofhours(Pickup_Date_Date, DropOff_Date_Date) - 24 * num_days - 24 * 7 * num_weeks;

            String[] FeeRate = new String[6];
            FeeRate = getFeeRate(Vin);

            String FuelRate = FeeRate[7];
            String DistanceRate = FeeRate[6];

            String[][] DisplayFee = new String[15][3];

            DisplayFee[0][0] = "Normal:";
            DisplayFee[0][1] = Double.parseDouble(FeeRate[0]) + " x " + num_weeks
                    + "W " + Double.parseDouble(FeeRate[1]) + " x " + num_days + "D " + Double.parseDouble(FeeRate[2]) + " x " + num_hours+"H.";
            tempFee = ((Double.parseDouble(FeeRate[0]) * num_weeks) + (Double.parseDouble(FeeRate[1]) * num_days) + (Double.parseDouble(FeeRate[2]) * num_hours));
            DisplayFee[0][2] = "" + tempFee;
            totalFee += tempFee;
            
            if (NumHour_OverDue > 0) {
                DisplayFee[1][0] = "(Over Due):";
                DisplayFee[1][1] = NumHour_OverDue + " x " + Double.parseDouble(FeeRate[2])+"H.";
                DisplayFee[1][2] = "" + NumHour_OverDue * Double.parseDouble(FeeRate[2]);
                totalFee += NumHour_OverDue * Double.parseDouble(FeeRate[2]);
            }
            
            num_weeks = getNumberofWeeks(Pickup_Date_Date, Drop);
            num_days = getNumberofDays(Pickup_Date_Date, Drop) - 7 * num_weeks;
            num_hours = getNumberofhours(Pickup_Date_Date, Drop) - 24 * num_days - 24 * 7 * num_weeks;
            DisplayFee[2][0] = "Insurance:";
            DisplayFee[2][1] = Double.parseDouble(FeeRate[3]) + " x " + num_weeks
                    + "W " + Double.parseDouble(FeeRate[4]) + " x " + num_days + "D " + Double.parseDouble(FeeRate[5]) + " x " + num_hours+"H.";
            InsuranceFee = ((Double.parseDouble(FeeRate[3]) * num_weeks) + (Double.parseDouble(FeeRate[4]) * num_days) + (Double.parseDouble(FeeRate[5]) * num_hours));
            DisplayFee[2][2] = "" + InsuranceFee;
             totalFee += InsuranceFee;
             
            //equipment part :
            int equ_length = getEquipInfo(Vin).length;
            String[] EquipInfo = new String[equ_length];
            EquipInfo = getEquipInfo(Vin);
            
            num_days = getNumberofDays(Pickup_Date_Date, Drop);
            num_hours = getNumberofhours(Pickup_Date_Date, Drop) - 24 * num_days;
            DisplayFee[4][0] = "Equipment:";
            if(equ_length == 0){
                DisplayFee[4][1] = "NA";
            }else if(equ_length == 4){
                DisplayFee[4][1] = "("+EquipInfo[0] + ": "+ EquipInfo[1]+" x "+num_days+"D + "+EquipInfo[2]+" x "+num_hours+"H ) x " + EquipInfo[3];
                tempFee = (Double.parseDouble(EquipInfo[1])*num_days+Double.parseDouble(EquipInfo[2])*num_hours)*Double.parseDouble(EquipInfo[3]);
                DisplayFee[4][2] =""+ tempFee;
                totalFee +=tempFee;
                
            }else if(equ_length==8){
                         DisplayFee[4][1] = EquipInfo[0] + ": ("+ EquipInfo[1]+" x "+num_days+"D + "+EquipInfo[2]+" x "+num_hours+"H ) x " + EquipInfo[3];
                tempFee = (Double.parseDouble(EquipInfo[1])*num_days+Double.parseDouble(EquipInfo[2])*num_hours)*Double.parseDouble(EquipInfo[3]);
                DisplayFee[4][2] =""+ tempFee;
                totalFee +=tempFee;
                
                DisplayFee[5][1] = EquipInfo[4] + ": ("+ EquipInfo[5]+" x "+num_days+"D + "+EquipInfo[6]+" x "+num_hours+"H ) x " + EquipInfo[7];
                tempFee = (Double.parseDouble(EquipInfo[5])*num_days+Double.parseDouble(EquipInfo[6])*num_hours)*Double.parseDouble(EquipInfo[7]);
                DisplayFee[5][2] =""+ tempFee;
                totalFee +=tempFee;
            }
             
            DisplayFee[6][0] = "Fuel: ";
            DisplayFee[6][1] = FuelRate + " x " + Fuel + ".";
             tempFee = Double.parseDouble(FuelRate) * Double.parseDouble(Fuel);
            DisplayFee[6][2] = "" + tempFee;
             totalFee += tempFee;

            DisplayFee[8][0] = "Extended Distance: ";
            DisplayFee[8][1] = "" + DistanceRate + " x " + getExtendedDistance(Vin, Distance)+"Miles";
            tempFee = getExtendedDistance(Vin, Distance) * Double.parseDouble(DistanceRate);
            DisplayFee[8][2] = "" + tempFee;
            totalFee +=tempFee;
            
            DisplayFee[10][0] = "Membership Redeem:";
            if(Reedem){
                int[] reedemInfo =new int[3];
               reedemInfo = getRedeemInfo(Vin);
            DisplayFee[10][1] = "You have: "+reedemInfo[0]+" points, and you can reedem "+reedemInfo[1]+" D";
             tempFee = reedemInfo[1]*Double.parseDouble(FeeRate[1]);
            DisplayFee[10][2] = "-"+tempFee;
             totalFee -= tempFee;
            }
            
            DisplayFee[12][0] = "Road Star Discount:";
            if(roadStar){
                DisplayFee[12][1] = "Insurance fee x 0.5";
                DisplayFee[12][2] = "-"+InsuranceFee*0.5;
                totalFee -= InsuranceFee*0.5;
            }
            
            DisplayFee[14][0] = "Total:";
            DisplayFee[14][2] = ""+totalFee;

            return DisplayFee;
        } 
        else {
            JOptionPane.showMessageDialog(null, "Please input the Vin.");
        }
        return null;
    }

    //Get all fee rate of a category, which is stored in the String 
    public String[] getFeeRate(String Vin) {
        String[] FeeRate = new String[8];
        try {
            ConnectDB.exeQuery("select FE.weekly_rates, FE.daily_rates, FE.hourly_rates, FE.weekly_insurance, FE.daily_insurance, "
                    + "FE.hourly_insurance, FE.perkm, G.gas_rate "
                    + "from fleet as FL, feature as FE, gas as G "
                    + "where FL.vin = '" + Vin + "'  and FL.category = FE.category and FE.gas_no = G.gas_no");
            if (ConnectDB.resultSet().next()) {
                for (int i = 0; i < 8; i++) {
                    FeeRate[i] = ConnectDB.resultSet().getString(i + 1);
                }
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FeeRate;
    }

    public double getExtendedDistance(String Vin, String OdeReading) {

        String OdeRecord = "0";
        String limit = "0";
        double extendedDis = 0;
        try {
            ConnectDB.exeQuery("select FL.odometer, FE.limit from fleet as FL, feature as FE "
                    + " where FL.vin = '" + Vin + "'  and FL.category = FE.category");
            if (ConnectDB.resultSet().next()) {
                OdeRecord = ConnectDB.resultSet().getString(1);
                limit = ConnectDB.resultSet().getString(2);
            }
            extendedDis = Double.parseDouble(OdeReading) - Double.parseDouble(OdeRecord) - Double.parseDouble(limit);
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (extendedDis <= 0) {
            return 0;
        } else {
            return extendedDis;
        }
    }

    public String[] getEquipInfo(String Vin) {
        ArrayList<String> EquipInfo = new ArrayList<>();
        
        String[] temp = new String[4];
        for (int i = 0; i<4 ;i++){
            temp[i] = "";
        }
        try {
            ConnectDB.exeQuery("select EqR.equipment_type, EqR.quantity "
                    + "from reserve as RE, equipment_reserved as EqR "
                    + "where RE.vin = '"+ Vin +"' and RE.status = 'rented' and RE.confirmation_no = EqR.confirmation_no");
         int i=0;
            while(ConnectDB.resultSet().next()){
                 temp[2*i] = ConnectDB.resultSet().getString(1);
                 temp[2*i+1] = ConnectDB.resultSet().getString(2);
                 i++;
         }
         
         ConnectDB.clearResultSet();
         // get equipment fee rate:
         int j =0;
         while(j<4 &&!temp[j].isEmpty()){
                ConnectDB.exeQuery("select daily_rates, hourly_rates from equipment_fee where type = '"+temp[j]+"'");
                if (ConnectDB.resultSet().next()) {
                    EquipInfo.add(temp[j]);
                    EquipInfo.add(ConnectDB.resultSet().getString(1));
                    EquipInfo.add(ConnectDB.resultSet().getString(2));
                    EquipInfo.add(temp[j+1]);
                    j++;
                    j++;
                }
                ConnectDB.clearResultSet();
            }
         
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int length = EquipInfo.toArray().length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = EquipInfo.get(i);
        }
        return  result;
    }
    
    public int getEquipNum(String equip, String branch_id){
        int equipNum = 0;
        try {
            ConnectDB.exeQuery("select Units from equipment where equipment_type = '"+equip+"' and branch_id = '"+branch_id+"'");
            if(ConnectDB.resultSet().next()){
                String eqNum = ConnectDB.resultSet().getString(1);
                equipNum = Integer.parseInt(eqNum);
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        return equipNum;
    }
    
    
    //return an array (int), the first entry is the customer's points, the second entry is the days that he can redeem.
    // and the third entry is the customerID
    public int[] getRedeemInfo(String Vin){
        String[] points = new String[2];
        int[] points_int = new int[3];
        int pointsEX = 0;
        int mem_points = 0;
        int daysEX = 0;
        
        String carType = "";
        try {
            //?? not efficient.....
            ConnectDB.exeQuery("select ME.points, RE.customer_id from member as ME, reserve as RE where"
                    + " vin = '"+Vin+"' and RE.customer_id = ME.member_id");
            if (ConnectDB.resultSet().next()){
                points[0] = ConnectDB.resultSet().getString(1);
                points[1] = ConnectDB.resultSet().getString(2);
            }else{
                JOptionPane.showMessageDialog(null, "The customer has not registered");
            }
         ConnectDB.clearResultSet();
         
         carType = getVechileType(Vin);
         pointsEX = getVechilePointsEX(Vin);
         mem_points = Integer.parseInt(points[0]);
         daysEX = mem_points/pointsEX;
         
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        points_int[0] = Integer.parseInt(points[0]);
        points_int[1] = daysEX;
        points_int[2] = Integer.parseInt(points[1]);
        return points_int;
    }
    
    public String  getVechileType(String Vin){
        String type = "";
        try {
            ConnectDB.exeQuery("select category from fleet "
                    + "where vin = '"+Vin+"'");
            if(ConnectDB.resultSet().next()){
                type = ConnectDB.resultSet().getString(1);
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Car type is "+type);
        return type;
    }
    
     public int getVechilePointsEX(String Vin){
        String pointsEx = "";
        String type = getVechileType(Vin);
        try {
            ConnectDB.exeQuery("select points from points_exchange "
                    + "where category = '"+type+"'");
            if(ConnectDB.resultSet().next()){
                pointsEx = ConnectDB.resultSet().getString(1);
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Integer.parseInt(pointsEx);
    }
    
     // Function return the Staff id and branch id of the username.
    public String[] getStaffInfo(String username){
        String[] StaffInfo = new String[2];
        StaffInfo[0] = "";
        StaffInfo[1] = "";
        
        try {
            ConnectDB.exeQuery("select staff_id, branch_id from Accounts where username = '"+username+"'");
            if(ConnectDB.resultSet().next()){
                StaffInfo[0] = ConnectDB.resultSet().getString(1);
                StaffInfo[1] = ConnectDB.resultSet().getString(2);
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StaffInfo;
    }
}     

