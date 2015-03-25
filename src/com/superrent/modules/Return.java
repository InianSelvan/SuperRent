/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Period;

/**
 *
 * @author Iniyan
 */
public class Return {
    
    public int getNumberofWeeks(Date pickupdt, Date returndt){
        int weeks = 0;
        if(getNumberofhours(pickupdt, returndt)%7>1){
            weeks = getNumberofhours(pickupdt, returndt)%7;            
        }
        return weeks;
    }
    
    public int getNumberofDays(Date pickupdt, Date returndt){
        int days = 0;
        if(getNumberofhours(pickupdt, returndt)%24>1){
            days = getNumberofhours(pickupdt, returndt)%24;            
        }
        return days;
    }
    
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

}
