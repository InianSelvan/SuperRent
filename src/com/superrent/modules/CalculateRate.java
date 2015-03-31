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
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yaoyaolin
 */
public class CalculateRate {
    
    public static double CalculateNormalRate(Date date_start, Date date_end,int pick_time,int drop_time, float weekly_rate, float daily_rate, float hourly_rate){
        double result;

        // caulculate number of weeks
        int num_weeks = (int)((date_start.getTime() - date_end.getTime())/(1000*3600*24*7));   
        result =  weekly_rate * num_weeks;
        // caulculate number of days
        int num_days = (int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))/24 - 7*num_weeks; 
        result +=  daily_rate * num_days;
        //calculate number of hours
        int num_hour =(int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))%24;
        result += hourly_rate*num_hour;
  
        return result;
    }
   
    public static double CalculateInsRate(Date date_start, Date date_end,int pick_time,int drop_time, float weekly_rate, float daily_rate, float hourly_rate, boolean star){
        double result;

        // caulculate number of weeks
        int num_weeks = (int)((date_start.getTime() - date_end.getTime())/(1000*3600*24*7));   
        result =  weekly_rate * num_weeks;
        // caulculate number of days
        int num_days = (int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))/24 - 7*num_weeks; 
        result +=  daily_rate * num_days;
        //calculate number of hours
        int num_hour =(int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))%24;
        result += hourly_rate*num_hour;
  
        if (star == true){
            return (float)(0.5*result);
        }else{
        return result;
        }
    }
    
    public static double CalculateEquipRate(Date date_start, Date date_end,int pick_time,int drop_time, float weekly_rate, float daily_rate, float hourly_rate){
        double result;
        // caulculate number of days
        int num_days = (int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))/24; 
        result =  daily_rate * num_days;
        //calculate number of hours
        int num_hour =(int) ((date_start.getTime() + pick_time*60*60*1000 - date_end.getTime() - drop_time*60*60*1000)/(1000*3600))%24;
        result += hourly_rate*num_hour;
  
        return result;
    }
     
}
