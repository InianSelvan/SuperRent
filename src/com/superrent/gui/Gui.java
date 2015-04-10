/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.gui;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Iniyan
 */
public class Gui {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        
            SuperRent sr = new SuperRent();
            sr.setDefaultLookAndFeelDecorated(true);
            sr.setVisible(true);
            sr.setLocationRelativeTo(null);
      
    }       
}
