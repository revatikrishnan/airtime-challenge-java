package org.labyrinth.helper;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * 
 * @author Revati Krishnan Iyer
 *
 */
public class RESTHelper extends Configurations{

    /**
     * @param url
     * @return
     */
    public Response get(String url) {
        
       return RestAssured.given().header(getHeader()).get(url);
        
    }
    
    /**
     * 
     * @param json
     * @param url
     * @return
     */
    public Response post(String json, String url) {
        Response res1 = null;
        try {
           
            res1 = RestAssured.given().header(getHeader()).contentType("application/json").body(json).post(url);
        }
        catch (Exception e) {
           System.out.println("Exception occured "+e.getMessage());
        }
        return res1;
    }

    

    
}
