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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author miaowu
 */
public class ManageFleet {

    int car_or_truck;

    public void getCar_or_truck(int car_or_truck) {

        //car is 0 and truck is 1
        this.car_or_truck = car_or_truck;
    }

    public void fillManufacturer(JComboBox modelCombo) {
        String[] modelArray = {"Maserati", "Honda", "Infinity", "BMW", "Benz", "Acura", "GMC", "Toyota", "other"};
        int model_no;
        for (model_no = 0; model_no < 9; model_no++) {
            modelCombo.addItem(modelArray[model_no]);
        }

    }

    public void fillBranch(JComboBox branchidCombo) throws ClassNotFoundException, SQLException, IOException {
        ConnectDB.exeQuery("SELECT distinct branch_id FROM branch");
        while (ConnectDB.resultSet().next()) {
            int branchid = ConnectDB.resultSet().getInt("branch_id");
            branchidCombo.addItem(branchid);
        }
        ConnectDB.clearResultSet();
    }

    public void fillColor(JComboBox colorCombo) {
        String[] colorArray = {"black", "blue", "brown", "grey", "red", "orange", "silver", "white", "yellow", "other"};
        int color_no;
        for (color_no = 0; color_no < 10; color_no++) {
            colorCombo.addItem(colorArray[color_no]);
        }
    }
    
    
    
    
        public void fillCarType(JComboBox cartypeCombo) {
        String[] carTypeArray = {"Economy", "Compact", "Mid-size", "Standard", "Full-size", "Premium", "Luxury", "SUV", "Van"};
        String[] truckTypeArray = {"24-foot", "15-foot", "12-foot", "Box-Trucks", "Cargo-Vans"};
        int carType_no;
        if (car_or_truck == 0) {
            for (carType_no = 0; carType_no < 9; carType_no++) {
                cartypeCombo.addItem(carTypeArray[carType_no]);
            }
        } else if (car_or_truck == 1) {
            for (carType_no = 0; carType_no < 5; carType_no++) {
                cartypeCombo.addItem(truckTypeArray[carType_no]);
            }
        }
    }    

    public void fillsCarType(JComboBox cartypeCombo) throws ClassNotFoundException, SQLException, IOException {
        ConnectDB.exeQuery("SELECT distinct category FROM fleet where status = 'sale'");
        cartypeCombo.addItem("All");
        while (ConnectDB.resultSet().next()) {
            String scategory = ConnectDB.resultSet().getString("category");
            cartypeCombo.addItem(scategory);
        }
        ConnectDB.clearResultSet();

    }
    
    
       public void fillssModel(JComboBox modelCombo, String manufacturer,String cartype) throws ClassNotFoundException, SQLException, IOException {
        
           if (cartype=="All")
               cartype = "1=1";
           else 
               cartype = "category ='" + cartype + "'";
           if(manufacturer == "All")
               manufacturer = "1=1";
           else 
               manufacturer = "maker ='"+manufacturer + "'";
       
           String sql = new String();

            sql="SELECT distinct model FROM fleet where status = 'sale'and "+ manufacturer +" and " +cartype;
       
        ConnectDB.exeQuery(sql);
        modelCombo.addItem("All");
        while (ConnectDB.resultSet().next()) {
            String smodel = ConnectDB.resultSet().getString("model");
            System.out.println(smodel);
            modelCombo.addItem(smodel);
        }
        ConnectDB.clearResultSet();
    }
       
       
    public void fillssManufacturer(JComboBox manufacturerpeCombo, String cartype) throws ClassNotFoundException, SQLException, IOException {
        String sql = new String();
        if (cartype=="All")
               cartype = "1=1";
           else 
               cartype = "category ='" + cartype + "'";
         
            sql = "SELECT distinct maker FROM fleet where status = 'sale' and "+cartype ;
        ConnectDB.exeQuery(sql);
        manufacturerpeCombo.addItem("All");         
        while (ConnectDB.resultSet().next()) {
            String smanufacturer = ConnectDB.resultSet().getString("maker");
            manufacturerpeCombo.addItem(smanufacturer);
        }
        ConnectDB.clearResultSet();

    }
       

       String sqlforInfo = new String();
       
       public void setsqlinfo (int i,String cartype, String manufacturer, String model){
           if (cartype=="All")
               cartype = "1=1";
           else 
               cartype = "category ='" + cartype + "'";
           if(manufacturer == "All")
               manufacturer = "1=1";
           else 
               manufacturer = "maker ='"+manufacturer + "'";
           if(model == "All")
               model = "1=1";
           else model = "model='"+model+"'";
           
           if(i==0){
               sqlforInfo = "select vin, branch_id, category, maker, model, year from fleet where status ='sale'";
           }
           
           else if(i==1){
              // if(cartype != "All"){
               sqlforInfo = "SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale' and  "+cartype ;
              // }
//               else 
//                   sqlforInfo = "SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale' ";
               }
           
           else if(i==2){
              // if(manufacturer!="All"){
                   sqlforInfo = "SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale' and  " + manufacturer + " and  " + cartype ;
              // }
//               else
//                   sqlforInfo ="SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale' and category = '" + cartype + "'";
           }
           
           else if(i==3){
            //   if(model != "All"){
                   sqlforInfo = "SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale' and " + model + " and " + manufacturer + " and " + cartype;
              // }
//               else 
//                    sqlforInfo = "SELECT  vin, branch_id, category, maker, model, year FROM fleet where status = 'sale'and maker = '" + manufacturer + "'"+"and category = '" + cartype + "'";                
           }

       }
        public String[][] getonsaleInfo() {
        String Onsaleinfo[][] = new String[100][6];
        try {
            int i = 0;
            ConnectDB.exeQuery(sqlforInfo);

            while (ConnectDB.resultSet().next()) {
                for (int j = 0; j < 6; j++) {
                    Onsaleinfo[i][j] = ConnectDB.resultSet().getString(j + 1);
                    //System.out.println(ConnectDB.resultSet().getString(j+1));
                }
                i++;
            }
            ConnectDB.clearResultSet();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SuperRent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Onsaleinfo;
    }

    public void clearTable(JTable mfremoveTable) {
        //To change body of generated methods, choose Tools | Templates.
        DefaultTableModel model =(DefaultTableModel) mfremoveTable.getModel();
                while(model.getRowCount()>0){
                model.removeRow(model.getRowCount()-1);
                }
    }

    public void settable(int i,String cartype, String manufacturer, String model, JTable mfremoveTable) {
       clearTable(mfremoveTable);
       setsqlinfo(i,cartype,manufacturer,model);
       //System.out.println(sqlforInfo);
       mfremoveTable.setModel(new javax.swing.table.DefaultTableModel(getonsaleInfo(),
                new String[]{
                        "VIN", "Branch", "Car Type", "Manufacturer", "Model", "Year"
                }));
    }
        public void remove(JTable mfremoveTable) throws ClassNotFoundException, SQLException, IOException {
            
            String info[] = new String[6];
            for(int i=0;i<6;i++){
                info[i]=(String) mfremoveTable.getValueAt(mfremoveTable.getSelectedRow(),i);
                //System.out.println(info[i]);
            }
            
            ConnectDB.exeUpdate("update fleet set status = 'sold' where vin='"+info[0]+"'");
            

    }  
    


}
