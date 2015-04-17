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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alexander
 */
public class NewEmployee {
    public DefaultTableModel getAllEmployee() throws ClassNotFoundException, SQLException, IOException{
        
        
        
        String[][] VehicleInfo=null;
        String[] col = new String[7];
        
        try{
            ConnectDB.exeQuery("Select staff_id, email,username,lastname,firstname,role,branch_id from Accounts ");

           
        ResultSetMetaData md = ConnectDB.resultSet().getMetaData();
        for (int i=0; i<md.getColumnCount(); i++){
            col[i] = md.getColumnName(i+1);      
        }

            
        VehicleInfo= new String[500][7];
            for(int i=0; ConnectDB.resultSet().next(); i++){
                for(int j=1; j<=7; j++){

                    VehicleInfo[i][j-1]= ConnectDB.resultSet().getString(j); 
                   
                }
            }        
        }
        finally{
            ConnectDB.clearResultSet();  
        }
        return new DefaultTableModel(VehicleInfo, col); 
    }
    
    public void delEmployee(String staffid){
        try {
            
            ConnectDB.exeUpdate("DELETE FROM Accounts WHERE staff_id ='"+staffid+"'");
            ConnectDB.clearResultSet();
            
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(NewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public void modEmployee(String password, String staffid, String email, String username, String firstname, String lastname, String role, String branch){
        try {

                ConnectDB.exeUpdate("update Accounts set password = '"+password+"', email = '"+email+"', username = '"+username+"' , lastname ='"+lastname+"', firstname = '"+firstname+"', role = '"+role+"' ,branch_id='"+branch+"' where staff_id='"+staffid+"'");
                System.out.println("update Accounts set password = '"+password+"', email = '"+email+"', username = '"+username+"' , lastname ='"+lastname+"', firstname = '"+firstname+"', role = '"+role+"' ,branch_id='"+branch+"' where staff_id='"+staffid+"'");
            ConnectDB.clearResultSet();
        } catch (SQLException ex) {
            Logger.getLogger(NewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
                Logger.getLogger(NewEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
}
