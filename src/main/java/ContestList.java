import org.json.simple.*;
import org.json.simple.parser.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;

public class ContestList extends ArrayList<Contest>{
    private static final ArrayList<String> MyPlatforms=new ArrayList<String>(){{
        add("codechef");add("codeforces");add("hackerrank");add("hackerearth");add("topcoder");add("leetcode");add("atcoder");
    }};

    public void getContests() throws Exception{
        // Connect to API
        URL API=new URL("http://api.codercalendar.io/");
        HttpURLConnection connection = (HttpURLConnection) API.openConnection();
        connection.connect();
        // Read Data
        JSONObject obj = (JSONObject)new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
        JSONArray upcoming=(JSONArray)((JSONObject) obj.get("results")).get("upcoming");
        Iterator<JSONObject> iterator = upcoming.iterator();
        // Add to List
        while (iterator.hasNext()) {
            JSONObject tcntst=iterator.next();
            Contest temp=new Contest((String) tcntst.get("name"),new DateTime((long)tcntst.get("startTime")*1000),new DateTime((long)tcntst.get("endTime")*1000),(String)tcntst.get("platform"),(String)tcntst.get("url"));
            // Check if contest is useful
            if(temp.isUseful()){
                this.add(temp);
            }
        }
    }
    public int addMyContests(Calendar service) throws IOException{
        int retVal=0;
        for(Contest iterator: this){
            if(!iterator.contestExists(service)){
                for(String myplat:MyPlatforms){
                    if(iterator.platform.equalsIgnoreCase(myplat)){
                        iterator.addEventToCalender(service);
                        retVal++;
                    }
                }
            }
        }
        return retVal;
    }
}