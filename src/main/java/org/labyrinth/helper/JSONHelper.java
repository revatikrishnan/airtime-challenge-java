package org.labyrinth.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

/**
 * 
 * @author Revati Krishnan Iyer
 *
 */
public class JSONHelper extends RESTHelper{

    JsonPath jp;
    // Storing the roomid (which has light) and their respective order from the wall json
    SortedMap<String, Integer> lightRoomsAndTheirOrder = new TreeMap<String, Integer>();
    
    // sorting the roomids accoring to their order from the sorted map and storing the roomIds
    ArrayList<String> lightRoomsInOrder=new ArrayList<String>();
    /**
     * 
     * @param roomId
     * @return the array-list of all the available exits for the room
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getAllExitsForThisRoomId(String roomId) {
        
        Response res=get(getExitUrl()+"?roomId="+roomId);
        String exitsAvaialable=res.asString();
        //System.out.println("EXITS for "+roomId+" is "+exitsAvaialable);
        jp=new JsonPath(exitsAvaialable);
        HashMap<Object, Object> exits=jp.get();
        ArrayList<String> allExits=(ArrayList<String>)exits.get("exits");
        return allExits;
        
    }

    /**
     * 
     * @return the room if from start url
     */
    public String getRootRoomIdFromStartURL(){
        Response res= get(getStartUrl());
        return getRoomId(res.asString());
    }
    
    /**
     * 
     * @param roomId
     * @param eachExit
     * @return the response which we get when we move to an exit in the room
     */
    public Response moveToExit(String roomId, String eachExit){
        String url=getMoveUrl()+"?roomId="+roomId+"&exit="+eachExit;
        
        Response res1=get(url);
        return res1;
                        
        }
    
    /**
     * 
     * @return set of non-duplicate roomids
     */
    public Set<String> extractAllRoomIds(){
     // Prepare a list that would contains all the roomids after the extraction logic
        ArrayList<String> roomids=new ArrayList<String>();
        
        String rootRoomId=getRootRoomIdFromStartURL(); // Get the first room id 
        roomids.add(rootRoomId); // and add it to the list
        
        ArrayList<String> allExits;
        
        for(int eachRoomId=0;eachRoomId<roomids.size();eachRoomId++){
            
            allExits= getAllExitsForThisRoomId(roomids.get(eachRoomId));
            
            if(allExits!=null){
                for(String eachExit:allExits)
                {
                    Response response=moveToExit(roomids.get(eachRoomId), eachExit);
                    // if moving to an exit does not result in an error, it is the room whose id should be added to my list of roomids
                    if(!(response.asString().contains("404"))){
                        roomids.add(getRoomId(response.asString()));
                    }
                }
            }
        }
        
        //remove the duplicates from the roomids list just in case
        return new HashSet<String>(roomids);
    }
    
    /**
     * 
     * @param roomId
     * @return the response we get when we hit wall api
     */
    public Response hitTheWallApiForLightDetails(String roomId){
        Response res=get(getWallUrl()+"?roomId="+roomId);
        return res;
    }
    
    /**
     * 
     * @param roomJson
     * @return the extracted roomid from the room json
     */
    public String getRoomId(String roomJson){
        
        jp=new JsonPath(roomJson);
        HashMap<Object, Object> room=jp.get();
        String roomId=room.get("roomId").toString();
        
        return roomId;
    }

    /**
     * 
     * @return a list of light broken room's ids
     */
    public ArrayList<String> extractAlltheLightLessRoomIds() {
        Set<String> refinedRoomIds = extractAllRoomIds();
        
       
        ArrayList<String> lightLessRoomsInOrder=new ArrayList<String>();
        
        for(String eachRoomId:refinedRoomIds){
            Response res1=hitTheWallApiForLightDetails(eachRoomId);
            String lightJson=res1.asString();
            jp=new JsonPath(lightJson);
            HashMap<Object, Object> writeOrder=jp.get();
            
            String writing=writeOrder.get("writing").toString();
            Integer order=Integer.parseInt(writeOrder.get("order").toString());
            
            if(writing.equals("xx")&&order==-1){
                lightLessRoomsInOrder.add(eachRoomId);  
            }
            else{
                lightRoomsAndTheirOrder.put(eachRoomId, getOrderOfRoom(eachRoomId));
            }
        }
        
        SortedSet<Map.Entry<String, Integer>> sortedset = new TreeSet<Map.Entry<String, Integer>>(
                (e1,e2) ->e1.getValue().compareTo(e2.getValue()) );
        
        System.out.println("LIGHT ROOM AND ORDER "+lightRoomsAndTheirOrder);
        sortedset.addAll(lightRoomsAndTheirOrder.entrySet());
        
        
        for(Map.Entry<String, Integer> s : sortedset){
            lightRoomsInOrder.add(s.getKey());
        }
        System.out.println("LIGHT ROOM IN ORDER "+lightRoomsInOrder);
        return lightLessRoomsInOrder;
    }

    public String getTheChallengeCodeByConcatenation(){
        String challengecd="";
        for(String roomId:lightRoomsInOrder){
            if(challengecd=="")
                challengecd=getWritingOfRoom(roomId);
            else
                challengecd=challengecd+getWritingOfRoom(roomId);
            
        }
        return challengecd;
    }
    
    
    public String getWritingOfRoom(String roomId){
        Response res1=hitTheWallApiForLightDetails(roomId);
        String lightJson=res1.asString();
        jp=new JsonPath(lightJson);
        HashMap<Object, Object> writeOrder=jp.get();
        String writing=writeOrder.get("writing").toString();
        return writing;
    }
    
    
    public Integer getOrderOfRoom(String roomId){
        Response res1=hitTheWallApiForLightDetails(roomId);
        String lightJson=res1.asString();
        jp=new JsonPath(lightJson);
        HashMap<Object, Object> writeOrder=jp.get();
        Integer order=Integer.parseInt(writeOrder.get("order").toString());
        return order;
    }
    /**
     * 
     * @param lightLessRooms
     * @param challengeCode
     * @return a string representation of JSON with an array of light-less rooms and a challenge code 
     */
    public String generatetheReportJson(ArrayList<String> lightLessRooms, String challengeCode) throws JSONException{
        JSONObject report=new JSONObject();
        JSONArray lighLess=new JSONArray(lightLessRooms);
        report.put("roomIds", lighLess);
        report.put("challenge", challengeCode);
        
        System.out.println("Report Json Generated "+report.toString(2));
        return report.toString();
        
    }

    /**
     * 
     * @param reportJson
     * @return the statusCode of the post request
     */
    public int postJsonToReportAPI(String reportJson) {
        return post(reportJson, getReportUrl()).statusCode();
        
    }

}
