import org.json.simple.*;
import org.json.simple.parser.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;
import java.util.Iterator;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;

public class ContestList extends ArrayList<Contest> {
    private static final long serialVersionUID = 3L;
    public static final String UPOLADED_CONTESTS_PATH="user/uploadedContests";
    public static HashSet<Contest> uploadedContestList;

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

    public void getUploadedContests(){
        try {
			FileInputStream fis = new FileInputStream(ContestList.UPOLADED_CONTESTS_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			uploadedContestList = (HashSet<Contest>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
            uploadedContestList=new HashSet<>();
        }
        
    }

    public void setUploadedContests(){
        try {
            uploadedContestList.addAll(this);
			FileOutputStream fos = new FileOutputStream(ContestList.UPOLADED_CONTESTS_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(uploadedContestList);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
    }

    public int addMyContests(Calendar service) throws IOException {
        getUploadedContests();
        int addCount = 0;
        for (Contest iterator : this) {
            if(!uploadedContestList.contains(iterator)){
                if (!iterator.contestExists(service)) {
                    if (iterator.isUseful()){
                        iterator.addEventToCalender(service);
                        addCount++;
                    }
                }
            }
        }
        setUploadedContests();
        return addCount;
    }
}

//by shubh-shah
