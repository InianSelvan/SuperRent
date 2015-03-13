/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superrent.configs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Iniyan
 */
public class ReadProperties {
    /**
     * This method is to read the configurations of the properties file
     * @return returns the property object
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static Properties readFile() throws FileNotFoundException, IOException{
        Properties prop = new Properties();
        FileInputStream fp = new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\superrent\\configs\\Config.properties");
        prop.load(fp);
        return prop;
    }
    /**
     * By passing the key value it can read the properties value
     * @param key
     * @return
     * @throws IOException 
     */
    
    public static String getPropValue(String key) throws IOException{
        return readFile().getProperty(key);
    }
    
}
