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
import java.util.Properties;
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
    public static int compareDates(Date first, Date second){
        DateTime firstdt = new DateTime(first);
        Date fdt = firstdt.toLocalDate().toDate();
        DateTime seconddt = new DateTime(second);
        Date sdt = seconddt.toLocalDate().toDate();        
       return fdt.compareTo(sdt);
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
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
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
}
