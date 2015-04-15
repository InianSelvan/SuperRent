/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JTextField;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author Iniyan
 */
public class RentNGTest {
    Rent rent;
    JTextField Phone= new JTextField();
    public RentNGTest() {
    }
    @BeforeTest
    public void init(){
        rent = new Rent();
        Phone.setText("8867810252");
    }
    
    @Test
    public void testSomeMethod() throws SQLException, ClassNotFoundException, IOException {
        
        int actual = rent.validateConfirmationNo(Phone);
        int expected = 1;
        Assert.assertEquals(actual, expected, "Data is present");
    }
    
}
