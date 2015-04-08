/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
    
    public void rentVehicle(JTextField dl, JTextField ConfirmationNo){
        try {
                ConnectDB.exeUpdate("INSERT INTO `team01`.`rent` (`confirmation_no`, `driver_license`) VALUES ('"+ConfirmationNo.getText().toString()+"', '"+dl.getText().toString()+"')");
                ConnectDB.exeUpdate("UPDATE `team01`.`reserve` SET `status`='rented' WHERE  `confirmation_no`='"+ConfirmationNo.getText().toString()+"';");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Rent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Rent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Rent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }   
}
