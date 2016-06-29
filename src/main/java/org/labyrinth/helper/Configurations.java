package org.labyrinth.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import com.jayway.restassured.response.Header;

/**
 * 
 * @author Revati Krishnan Iyer
 *
 */
public class Configurations {

    // Get all the configurations values from the properties files
    private String mainUrl=readProperties("BaseURL");
    private String startUrl=mainUrl + readProperties("StartURL");
    private String exitUrl=mainUrl + readProperties("ExitUrl");
    private String moveUrl=mainUrl + readProperties("MoveUrl");
    private String wallUrl=mainUrl + readProperties("WallUrl");
    private String reportUrl = mainUrl+ readProperties("ReportUrl");      
    private String headerKey=readProperties("HeaderKey");
    private String headerValue=readProperties("HeaderValue");
    
    public String getReportUrl(){
        return reportUrl;
    }
    
    public String getWallUrl() {
        return wallUrl;
    }

    public String getStartUrl() {
        return startUrl;
    }

    public String getExitUrl() {
        return exitUrl;
    }
    
    public String getMoveUrl() {
        return moveUrl;
    }

   
    public Header getHeader(){
        Header header = new Header(headerKey,headerValue );
        return header;
    }

    
    public static String readProperties(String key){
        Properties prop=new Properties();
        
        
        String value="";
        try {
            
            InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            
            prop.load(resourceStream);
            value=prop.getProperty(key);
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        return value;
        
            
         
    }
}
