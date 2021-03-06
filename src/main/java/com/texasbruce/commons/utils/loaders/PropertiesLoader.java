package com.texasbruce.commons.utils.loaders;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {
    
    private static final Long RELOAD_TIMER_INTERVAL = 30000L;

    private static Logger logger = Logger.getLogger(PropertiesLoader.class);

    private static PropertiesLoader instance = new PropertiesLoader();

    private Properties properties = null;
    private ArrayList<Runnable> handlers  = new ArrayList<Runnable>();
    private long lastModified = 0L;
    private String filename = "tme.properties";
    
    private PropertiesLoader () {

        reloadProperties();

        new java.util.Timer().schedule(new java.util.TimerTask(){

            @Override
            public void run() {
                reloadProperties();
            }}, 0, RELOAD_TIMER_INTERVAL);
    }
    
    public static PropertiesLoader getInstance () {
        return instance;
    }
    
    public void setFilename (String _filename) {
        this.filename = _filename;
    }
   
    private void reloadProperties() {
        java.io.File file = new java.io.File(System.getProperty("jboss.server.home.dir") + "/properties/" + filename);
        
        if (!file.exists()) {
            file = new java.io.File(filename);
        }
        
        if (file.lastModified() != lastModified) {
            logger.info("Properties file " + file.getAbsolutePath() + " changed. Reloading");
     
            properties = new Properties();
            
            try {
                properties.load(new FileInputStream(file));
            } catch (Exception e) {
                logger.info("Could not find JBoss tme.properties. Using root :" + e);
                
                try {
                    properties.load(new FileInputStream(file));
                } catch (Exception ex) {
                    logger.error("reloadProperties", ex);
                }
            }
            
            for (Runnable handler : handlers) {
                handler.run();
            }           
            lastModified = file.lastModified();
        }
    }
        
    public String getProperty (String propertyName)  {
        return properties.getProperty(propertyName);
    }

    public String getProperty (String propertyName, String defaultValue)  {
        return properties.getProperty(propertyName, defaultValue);
    }
    
    public Enumeration<?> getPropertyNames () {
        return properties.propertyNames();
    }
    
    public void addPropertiesReloadHandler (Runnable callback) {
        handlers.add(callback);
    } 
}
