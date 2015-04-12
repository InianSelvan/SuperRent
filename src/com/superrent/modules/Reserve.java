/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;
 
import com.superrent.DataBase.ConnectDB;
import com.superrent.gui.SuperRent;
import com.toedter.calendar.JDateChooser;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Iniyan
 */
public class Reserve {
     
    public void fillBranchIDCombo(JComboBox branchCombo) throws ClassNotFoundException, SQLException, IOException{
        try{ 
            ConnectDB.exeQuery("SELECT branch_id FROM branch");
        
            while(ConnectDB.resultSet().next()){
                int branchId = Integer.parseInt(ConnectDB.resultSet().getString("branch_id"));
                branchCombo.addItem(branchId);
             }
            
        }
        finally{
            ConnectDB.clearResultSet();
        }
    } 
   
    public void fillVehicleCatCombo(JComboBox vehicleCatCombo, JComboBox branchCombo, JComboBox vehicleType) throws ClassNotFoundException, SQLException, IOException{
        if(!branchCombo.getSelectedItem().toString().equals("Select")){
            int branchId = Integer.parseInt(branchCombo.getSelectedItem().toString());
            String VehicleType = vehicleType.getSelectedItem().toString();
            try{
                ConnectDB.exeQuery("SELECT category FROM fleet WHERE branch_id = "+branchId+" and car_or_truck ='"+VehicleType+"'");
                while(ConnectDB.resultSet().next()){
                    String carType = ConnectDB.resultSet().getString("category");
                    vehicleCatCombo.addItem(carType);
                }
            }finally{
                ConnectDB.clearResultSet();
            }
        }
    }
    
        
    
    public DefaultListModel fillEquipmentList(JList equipments, JComboBox cartypeCombo, JComboBox branchid ) throws ClassNotFoundException, SQLException, IOException, InterruptedException{

        DefaultListModel listEquip= new DefaultListModel();
        try{
            String vehicalType = null;
            
            String branch_id = branchid.getSelectedItem().toString();
            if(cartypeCombo.getSelectedItem()!=null && branchid.getSelectedItem()!="Select")
            {
                vehicalType = cartypeCombo.getSelectedItem().toString();
            }        
            ConnectDB.exeQuery("SELECT equipment_type from equipment where car_or_truck='"+vehicalType+"' and branch_id='"+branch_id+"'");
            while(ConnectDB.resultSet().next()){
                String EquipType = ConnectDB.resultSet().getString("equipment_type");
                listEquip.addElement(EquipType);
                
            }
        }finally{
            ConnectDB.clearResultSet();
        }
        
        return listEquip;
    }
    
    public int validateCustomerInfo(JTextField cusNumField) throws ClassNotFoundException, SQLException, IOException{
        int cusID =0;
        try{
            String phoneNum = cusNumField.getText();       
            ConnectDB.exeQuery("SELECT customer_id FROM customer WHERE phone ='"+phoneNum+"'");
            
            if(ConnectDB.resultSet().next()){
                cusID = Integer.parseInt(ConnectDB.resultSet().getString("customer_id"));
            }
        }finally{    
            ConnectDB.clearResultSet();
            return cusID;
        }
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

    public DefaultTableModel getAvailableVehicles(String vehicleCat, int branchId, String VehicleType, Date pickup, Date dropoff, String pickuptime, String dropofftime) throws ClassNotFoundException, SQLException, IOException{
        
        int num = countAvailableVehicles(vehicleCat, branchId, VehicleType, pickup, dropoff, pickuptime, dropofftime);
        
        String[][] VehicleInfo=null;
        String[] col = new String[5];
        
        try{
            ConnectDB.exeQuery("select v.vin, v.category, v.color, v.doors, v.branch_id  from fleet as v where vin not in (select vin from reserve where \n" +
                "(('"+pickup+" "+pickuptime+"' <= pickup_time and '"+dropoff+" "+dropofftime+"' >= pickup_time and '"+dropoff+" "+dropofftime+"' <= dropoff_time) or \n" +
                "('"+pickup+" "+pickuptime+"'>=pickup_time and '"+pickup+" "+pickuptime+"'<=dropoff_time and '"+dropoff+" "+dropofftime+"' <= dropoff_time and '"+dropoff+" "+dropofftime+"' >= pickup_time) or\n" +
                "('"+pickup+" "+pickuptime+"' <= dropoff_time and '"+dropoff+" "+dropofftime+"' >= dropoff_time)) and\n" +
                "(status = 'reserved' or status = 'rented')) and branch_id ='"+branchId+"' and category='"+vehicleCat+"' and car_or_truck = '"+VehicleType+"' ");

           
        ResultSetMetaData md = ConnectDB.resultSet().getMetaData();
        
        col[0] = md.getColumnName(1);
        col[1] = md.getColumnName(2);
        col[2] = md.getColumnName(3);
        col[3] = md.getColumnName(4);
        col[4] = md.getColumnName(5);
            
        VehicleInfo= new String[100][5];
            for(int i=0; ConnectDB.resultSet().next(); i++){
                for(int j=1; j<=5; j++){

                    VehicleInfo[i][j-1]= ConnectDB.resultSet().getString(j); 
                    
                }
            }        
        }
        finally{
            ConnectDB.clearResultSet();  
        }
        return new DefaultTableModel(VehicleInfo, col); 
    }
    
    public  int countAvailableVehicles(String vehicleCat, int branchId, String VehicleType, Date pickup, Date dropoff, String pickuptime, String dropofftime) throws ClassNotFoundException, SQLException, IOException{
        int countVehi=0;
        try{
            
            ConnectDB.exeQuery("select v.vin, v.category, v.color, v.doors, v.branch_id  from fleet as v where vin not in (select vin from reserve where \n" +
                "(('"+pickup+" "+pickuptime+"' <= pickup_time and '"+dropoff+" "+dropofftime+"' >= pickup_time and '"+dropoff+" "+dropofftime+"' <= dropoff_time) or \n" +
                "('"+pickup+" "+pickuptime+"'>=pickup_time and '"+pickup+" "+pickuptime+"'<=dropoff_time and '"+dropoff+" "+dropofftime+"' <= dropoff_time and '"+dropoff+" "+dropofftime+"' >= pickup_time) or\n" +
                "('"+pickup+" "+pickuptime+"' <= dropoff_time and '"+dropoff+" "+dropofftime+"' >= dropoff_time)) and\n" +
                "(status = 'reserved' or status = 'rented')) and branch_id ='"+branchId+"' and category='"+vehicleCat+"' and car_or_truck = '"+VehicleType+"' ");
           while(ConnectDB.resultSet().next()){
               countVehi = Integer.parseInt(ConnectDB.resultSet().getString(1));
           }
        }finally{
            ConnectDB.clearResultSet();
        }
        return countVehi;
    }

    public void cancelReservation(JTextField confirmationNo, JTextField phone, JDateChooser pickup, JDateChooser dropoff) throws ParseException{
        long count = 0;
   
        try {
            if((confirmationNo.getText().length()!=0 || phone.getText().length()!=0) && pickup.getDate()!=null && dropoff.getDate()!=null ){
                String pick = CommonFunc.changeDateFormat(pickup).toString();
                String drop = CommonFunc.changeDateFormat(dropoff).toString();
                ConnectDB.exeQuery("SELECT confirmation_no FROM reserve WHERE pickup_time like '"+pick+"%' "
                        + "and dropoff_time LIKE '"+drop+"%' and status = 'reserved' and "
                        + "(phone = '"+phone.getText()+"' or  confirmation_no ='"+confirmationNo.getText()+"')");
                
                while(ConnectDB.resultSet().next()){
                    count = Long.parseLong(ConnectDB.resultSet().getString(1));
                }
                ConnectDB.clearResultSet();
                if(count>0){
                    ConnectDB.exeUpdate("DELETE FROM reserve WHERE confirmation_no ="+count);
                    ConnectDB.clearResultSet();
                    ConnectDB.exeUpdate("DELETE FROM equipment_reserved WHERE confirmation_no = "+count );
                    ConnectDB.clearResultSet();
                    JOptionPane.showMessageDialog(null, "Reservation cancelled for "+count);
                }else{
                    JOptionPane.showMessageDialog(null, "Not a valid confirmation number/phone number");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Make sure all the required fields are filled");
            }
            } catch (ClassNotFoundException | SQLException | IOException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, ex);
                ex.printStackTrace();
            }
    }
    
}
