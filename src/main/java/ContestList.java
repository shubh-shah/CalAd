import org.json.simple.*;
import org.json.simple.parser.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.io.*;
import java.util.Iterator;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;

public class ContestList extends ArrayList<Contest> {
    private static final long serialVersionUID = 2L;

    public void getContests() throws Exception {
        URL API = new URL("http://api.codercalendar.io/");
        HttpURLConnection connection = (HttpURLConnection) API.openConnection();
        connection.connect();
        
        JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
        JSONArray upcoming = (JSONArray) ((JSONObject) obj.get("results")).get("upcoming");

        Iterator<JSONObject> iterator = (Iterator<JSONObject>) upcoming.iterator();
        while (iterator.hasNext()) {
            JSONObject tcntst = iterator.next();
            Contest temp = new Contest((String) tcntst.get("name"), new DateTime((long) tcntst.get("startTime") * 1000),
                    new DateTime((long) tcntst.get("endTime") * 1000), (String) tcntst.get("platform"),
                    (String) tcntst.get("url"));
            this.add(temp);
        }
    }

    public int addMyContests(Calendar service) throws IOException {
        int addCount = 0;
        for (Contest iterator : this) {
            if (!iterator.contestExists(service)) {
                if (iterator.isUseful()){
                    iterator.addEventToCalender(service);
                    addCount++;
                }
            }
        }
        return addCount;
    }
}

//by shubh-shah
