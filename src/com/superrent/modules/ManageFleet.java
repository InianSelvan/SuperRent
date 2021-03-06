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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author miaowu
 */
public class ManageFleet {
    private int car_or_truck;
    private String function;
    private String sqlforInfo = new String();
    
    public ManageFleet(String function) {
        //To change body of generated methods, choose Tools | Templates.
        this.function = function;
    }

    ManageFleet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
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
    
    
        public void fillratetype(JComboBox modelCombo) {
        String[] modelArray = {"daily_rates", "weekly_rates", "hourly_rates", "weekly_insurance", "daily_insurance", "hourly_insurance", "perkm", "gas_no", "limit"};
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
        ConnectDB.exeQuery("SELECT distinct year FROM fleet where status = '"+function+"' order by year" );
        cartypeCombo.addItem("All");
        while (ConnectDB.resultSet().next()) {
            String scategory = ConnectDB.resultSet().getString("year");
            cartypeCombo.addItem(scategory);
        }
        ConnectDB.clearResultSet();
    }
    public void fillssModel(JComboBox modelCombo, String manufacturer,String cartype) throws ClassNotFoundException, SQLException, IOException {
        if (cartype=="All")
            cartype = "1=1";
        else
            cartype = "F.year <='" + cartype + "'";
        if(manufacturer == "All")
            manufacturer = "1=1";
        else
            manufacturer = "F.category ='"+manufacturer + "'";
        String sql = new String();
        sql="SELECT distinct city FROM fleet F,branch B where F.branch_id = B.branch_id and F.status = '"+function+"' and "+ manufacturer +" and " +cartype;
        ConnectDB.exeQuery(sql);
        modelCombo.addItem("All");
        while (ConnectDB.resultSet().next()) {
            String smodel = ConnectDB.resultSet().getString("city");
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
            cartype = "year <='" + cartype + "'";
        sql = "SELECT distinct category FROM fleet where status = '"+function+"' and "+cartype ;
        ConnectDB.exeQuery(sql);
        manufacturerpeCombo.addItem("All");
        while (ConnectDB.resultSet().next()) {
            String smanufacturer = ConnectDB.resultSet().getString("category");
            manufacturerpeCombo.addItem(smanufacturer);
        }
        ConnectDB.clearResultSet();
    }
    
    public void setsqlinfo (int i,String cartype, String manufacturer, String model){
        if (cartype=="All")
            cartype = "1=1";
        else
            cartype = "year <='" + cartype + "'";
        if(manufacturer == "All")
            manufacturer = "1=1";
        else
            manufacturer = "category ='"+manufacturer + "'";
        if(model == "All")
            model = "1=1";
        else model = "city='"+model+"'";
        if(i==0){
            sqlforInfo = "select vin, branch_id, category, maker, model, year from fleet where status ='"+function+"'";
        }
        else if(i==1){
            // if(cartype != "All"){
            sqlforInfo = "SELECT vin, branch_id, category, maker, model, year FROM fleet where status = '"+function+"' and "+cartype ;
            // }
            // else
            // sqlforInfo = "SELECT vin, branch_id, category, maker, model, year FROM fleet where status = '"+function+"' ";
        }
        else if(i==2){
            // if(manufacturer!="All"){
            sqlforInfo = "SELECT vin, branch_id, category, maker, model, year FROM fleet where status = '"+function+"' and " + manufacturer + " and " + cartype ;
            // }
            // else
            // sqlforInfo ="SELECT vin, branch_id, category, maker, model, year FROM fleet where status = '"+function+"' and category = '" + cartype + "'";
        }
        else if(i==3){
            // if(model != "All"){
            sqlforInfo = "SELECT F.vin, F.branch_id, F.category, F.maker, F.model, F.year FROM fleet F, branch B where F.branch_id = B.branch_id and F.status = '"+function+"' and B." + model + " and F." + manufacturer + " and F." + cartype;
            // }
            // else
            // sqlforInfo = "SELECT vin, branch_id, category, maker, model, year FROM fleet where status = '"+function+"'and maker = '" + manufacturer + "'"+"and category = '" + cartype + "'";
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
        if(function =="sale"){
            ConnectDB.exeUpdate("update fleet set status = 'sold' where vin='"+info[0]+"'");
         ConnectDB.clearResultSet();
        }
        else if(function == "rent"){
            ConnectDB.exeUpdate("update fleet set status = 'sale' where vin='"+info[0]+"'");
         ConnectDB.clearResultSet();
        }
        
       
    }
    public void sendback(JTable mfremoveTable) throws ClassNotFoundException, SQLException, IOException {
        String info[] = new String[6];
        if(mfremoveTable.getSelectedRow()==-1){
           JOptionPane.showMessageDialog(null, "Please select a vehicle");
        }
        for(int i=0;i<6;i++){
            info[i]=(String) mfremoveTable.getValueAt(mfremoveTable.getSelectedRow(),i);
            //System.out.println(info[i]);
        }
        ConnectDB.exeUpdate("update fleet set status = 'rent' where vin='"+info[0]+"'");
         ConnectDB.clearResultSet();
    }
    
    
    public void showerror(String s){
        JOptionPane.showMessageDialog(null, s);
    }
    
    public void changepoint(double cash) throws ClassNotFoundException, IOException, SQLException{
        ConnectDB.exeUpdate("update cash_points set cash = "+cash);
        //ConnectDB.exeUpdate("update cash_points set points = 1");
         ConnectDB.clearResultSet();
        //  ConnectDB.clearResultSet();
        
    }

    public void managepoint(int i, int j) throws ClassNotFoundException, SQLException, IOException {
        System.out.println(i);
        ConnectDB.exeUpdate("update points_exchange set points ="+i+" where category = 'Economy'  OR category = 'Compact' OR category = 'Mid-size' OR category = 'Standard' OR category = 'Full-size' OR category = 'Mid-size' OR category = 'Standard' OR category = 'Premium'   ");
        
        ConnectDB.exeUpdate("update points_exchange set points ="+j+" where category = '24-foot'  OR category = '15-foot' OR category = '12-foot' OR category = 'Box-Trucks' OR category = 'Cargo-Vans' OR category = 'Luxury' OR category = 'SUV'  OR category = 'Van'  ");
       
      //  ConnectDB.exeUpdate("update points_exchange set points = "+j+"where");
    }

    public void filloldvalue(JLabel oldvaluejlabel) {

    }

    public void filloldvalue(JLabel oldvaluejlabel, String cartype, String ratetype,double i) throws ClassNotFoundException, SQLException, IOException {
        if(i==0){System.out.println(cartype+ ratetype);    
        String sql = "select "+ratetype+" from feature where category = '"+cartype+"'";
            //  String sql = "select "+ ratetype+"from feature where category = ' "+cartype+"'";
                      //     select daily_rates from feature where category = 'Mid-size';
            Double oldvalue = null;
            ConnectDB.exeQuery(sql);
        while (ConnectDB.resultSet().next()) {
            oldvalue = ConnectDB.resultSet().getDouble(ratetype);
            System.out.println(oldvalue);
//
        }
        ConnectDB.clearResultSet();
       
       
        oldvaluejlabel.setText(oldvalue.toString());
        }
        else {
            String sql = "update feature set "+ratetype+" = "+i+" where category = '"+cartype+"'";
            //update feature set daily_rates = 8 where category = 'Economy';
            ConnectDB.exeUpdate(sql);
            ConnectDB.clearResultSet();
            
        }
        
            
    }

    public void filleqp(JComboBox mfeqpcombo) {
         mfeqpcombo.addItem("ski_rack");
         mfeqpcombo.addItem("child_seat");
         mfeqpcombo.addItem("lift_gate");
         mfeqpcombo.addItem("car_tow");
         
    }
}