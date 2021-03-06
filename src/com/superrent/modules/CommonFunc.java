/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.modules;

import com.superrent.DataBase.ConnectDB;
import com.toedter.calendar.JDateChooser;
import java.io.IOException;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.swing.JSpinner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Iniyan
 */
public class CommonFunc {
    public static String username;//Yaoyao added
    
    public static Date changeDateFormat(JDateChooser jdate) throws ParseException{
        Date dt = jdate.getDate();
        java.sql.Date sqldate = new java.sql.Date(dt.getTime());
        return   sqldate;
    }
    public static int compareDates(String first, String second) throws ParseException{
        
        DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime firstdt = f.parseDateTime(first);
        DateTime seconddt = f.parseDateTime(second);
 
        
       return firstdt.compareTo(seconddt);
    }

    public static String sqlTime(JSpinner HH, JSpinner MM){
        String hh = String.valueOf(HH.getValue());
        String mm = String.valueOf(MM.getValue());
        String ss = "00";
        String time = hh+":"+mm+":"+ss;
        DateTimeFormatter f = DateTimeFormat.forPattern("HH:mm:ss");
        LocalTime tim = f.parseLocalTime(time);
        return tim.toString().substring(0, 8);
    }
    
    public static Date StringToDate(String MyDate) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date ResultDate = format.parse(MyDate);
        //System.out.println(MyDate);
        return ResultDate;
    }
    
     public static void triggerMail(String toPerson, String Subject, String Msg) {
      // Recipient's email ID needs to be mentioned.
      String to = toPerson;
      String from = "superrentt@gmail.com";
      final String username = "superrentt";
      final String password = "SuperRent123";

      
      String host = "smtp.gmail.com";

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject(Subject);

         // Now set the actual message
         message.setText(Msg);

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
   }
     public static String getEmail(String confirmationNo) throws SQLException{
         String email = "";
         try{ 
                 ConnectDB.exeQuery("SELECT email FROM customer where customer_id in (select distinct(customer_id) from reserve where"
                         + " confirmation_no = '"+confirmationNo+"')");
                         while(ConnectDB.resultSet().next()){
                email = ConnectDB.resultSet().getString("email");
             }
               
             
             } catch (ClassNotFoundException | SQLException | IOException ex) {
                 Logger.getLogger(CommonFunc.class.getName()).log(Level.SEVERE, null, ex);
             }finally{
                 ConnectDB.clearResultSet();
             }
        return email;
     }
     
     
}
