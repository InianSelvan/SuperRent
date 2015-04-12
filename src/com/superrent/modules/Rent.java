/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yaoyaolin
 */
public class Rent{
    
    public int validateConfirmationNo(JTextField ConfirmationNo) throws SQLException, ClassNotFoundException, IOException{
        int count = 0;
        try{ 
            ConnectDB.exeQuery("SELECT confirmation_no FROM reserve WHERE confirmation_no = '"+ConfirmationNo.getText()+"' AND status = 'reserved'");        
            while(ConnectDB.resultSet().next()){
                count++;
             }            
        }catch(NullPointerException e){
            JOptionPane.showMessageDialog(null, e);
        }
        finally{
            ConnectDB.clearResultSet();
        }  
        return count;
    }
    
    public int validateCustomerPhone(JTextField cusNumField) throws ClassNotFoundException, SQLException, IOException{
        int cusID =0;
        try{
            String phoneNum = cusNumField.getText();       
            ConnectDB.exeQuery("SELECT phone FROM reserve WHERE phone ='"+phoneNum+"' AND status = 'reserved'");
            
            if(ConnectDB.resultSet().next()){
                cusID = Integer.parseInt(ConnectDB.resultSet().getString("customer_id"));
            }
        }finally{    
            ConnectDB.clearResultSet();
            return cusID;
        }
    }
    
    public void rentVehicle(JTextField dl, String ConfirmationNo){
        try {
                ConnectDB.exeUpdate("INSERT INTO `team01`.`rent` (`confirmation_no`, `driver_license`) VALUES ('"+ConfirmationNo+"', '"+dl.getText()+"')");
                ConnectDB.exeUpdate("UPDATE `team01`.`reserve` SET `status`='rented' WHERE  `confirmation_no`='"+ConfirmationNo+"';");
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                Logger.getLogger(Rent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }   


        

public DefaultTableModel getReservedVehicles(String Confirm, String phone) throws ClassNotFoundException, SQLException, IOException{
                
        
        String[][] reservedInfo=null;
        String[] col = new String[7];
        
        try{
            ConnectDB.exeQuery("select confirmation_no, vin, customer_id, phone, pickup_time, dropoff_time, status FROM reserve \n" +
                            "WHERE (confirmation_no = '"+Confirm+"' or phone = '"+phone+"') and status = 'reserved'");
            
        ResultSetMetaData md = ConnectDB.resultSet().getMetaData();
        
        col[0] = md.getColumnName(1);
        col[1] = md.getColumnName(2);
        col[2] = md.getColumnName(3);
        col[3] = md.getColumnName(4);
        col[4] = md.getColumnName(5);
        col[5] = md.getColumnName(6);
        col[6] = md.getColumnName(7);
        
        
        reservedInfo= new String[100][7];
            for(int i=0; ConnectDB.resultSet().next(); i++){
                for(int j=1; j<=7; j++){

                    reservedInfo[i][j-1]= ConnectDB.resultSet().getString(j); 
                    
                }
            }        
        }
        finally{
            ConnectDB.clearResultSet();  
        }
        return new DefaultTableModel(reservedInfo, col); 
    }
}
