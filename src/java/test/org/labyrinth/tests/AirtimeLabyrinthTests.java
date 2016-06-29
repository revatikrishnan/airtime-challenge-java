package org.labyrinth.tests;

import java.util.ArrayList;
import org.json.JSONException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * @author Revati Krishnan Iyer
 *
 */
public class AirtimeLabyrinthTest extends TestBase {

    @Test
    public void extractLightLessRoomIdsAndPost(){
     
        ArrayList<String> lightLessRooms=jsonhelper.extractAlltheLightLessRoomIds();
        
        
        String challengeCode="7182";
        int expected = 200;
        
        try {
            
            String reportJson =jsonhelper.generatetheReportJson(lightLessRooms,challengeCode);
            
            int status=jsonhelper.postJsonToReportAPI(reportJson);
            
            Assert.assertEquals(status, expected);
        }
        catch (JSONException e) {
            
            System.out.println("Exception Occured "+e.getMessage());
        }
        
    }
}
