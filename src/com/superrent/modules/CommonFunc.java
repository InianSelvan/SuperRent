/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.toedter.calendar.JDateChooser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JSpinner;

/**
 *
 * @author Iniyan
 */
public class CommonFunc {
    public static Date changeDateFormat(JDateChooser jdate) throws ParseException{
        Date dt = jdate.getDate();
        java.sql.Date sqldate = new java.sql.Date(dt.getTime());
        return sqldate;
    }
    
    public static String sqlTime(JSpinner HH, JSpinner MM){
        String hh = String.valueOf(HH.getValue());
        String mm = String.valueOf(MM.getValue());
        String ss = "00";
        String time = hh+":"+mm+":"+ss;
        return time;
    }
    
    public static Date StringToDate(String MyDate) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date ResultDate = format.parse(MyDate);
        //System.out.println(MyDate);
        return ResultDate;
}
}
