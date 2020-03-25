import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contest{
  private static final String WORKING_CALENDAR="9uev9q2t70ucmi6trga7ta38js@group.calendar.google.com";
  public String name;
  public DateTime startTime;
  public DateTime endTime;
  public String platform,url;

  public Contest(String name,DateTime startTime,DateTime endTime,String platform,String url) {
    this.name = name.trim();
    this.startTime = startTime;
    this.endTime = endTime;
    this.platform = platform;
    this.url = url;
  }

  public boolean contestExists(Calendar service) throws IOException{ //Make this semi-offline
    Events dupl = service.events().list(WORKING_CALENDAR)
            .setTimeMax(endTime)
            .setTimeMin(startTime)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
    List<Event> items = dupl.getItems();
    if (!items.isEmpty()){
        for (Event event : items) {
            if (this.name.equalsIgnoreCase(event.getSummary())) {
              return true;
            }
        }
    }
    return false;
  }
  public void addEventToCalender(Calendar service) throws IOException{
    EventDateTime start = new EventDateTime().setDateTime(startTime);
    EventDateTime end = new EventDateTime().setDateTime(endTime);

    Event event = new Event()
        .setSummary(name)
        .setLocation(url)
        .setDescription(platform)
        .setStart(start)
        .setEnd(end);

    event = service.events().insert(WORKING_CALENDAR, event).execute();
  }
  public boolean isUseful(){
    String inp=this.name;
    if(inp.contains("Hiring")||inp.contains("hiring")||inp.contains("Hire")||inp.contains("Codefest")||inp.contains("codefest")||inp.contains("hire")||inp.contains("Hackathon")||inp.contains("hackathon")||inp.contains("Machine Learning")||inp.contains("machine learning")||inp.contains("Hackfest")||inp.contains("hackfest")||inp.contains("Airport")){
        return false;
    }
    else if((this.endTime.getValue()-this.startTime.getValue())/(1000*60*60*24)>14){   //length of contests
      return false;
    }
    return true;
  }
}