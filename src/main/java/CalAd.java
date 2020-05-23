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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Collections;

public class CalAd {
    private static final String APPLICATION_NAME = "CalAd";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    // Creates an authorized Credential object.
    // param HTTP_TRANSPORT The network HTTP Transport.
    // return An authorized Credential object.
    // throws IOException If the credentials.json file cannot be found.
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalAd.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Calendar connectToGoogle() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Calendar service;
        try{
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME).build();
            System.out.println("Connected to Calendar");
        }
        catch(IOException e){
            writeLog(0,false);
            System.out.println(e);
            throw e;
        }
        return service;
    }

    public static int pushContests(Calendar service) throws IOException {
        ContestList contests = new ContestList();
        try {
            contests.getContests();
        } catch (Exception e) {
            writeLog(1,false);
            System.out.println(e);
            System.exit(0);
        }
        System.out.println("Got Contests");
        int changes=0;
        try{
            changes = contests.addMyContests(service);
            System.out.println("Changes Made = " + changes);
        } catch(IOException e){
            writeLog(2,false);
            System.out.println(e);
            throw e;
        }
        return changes;
    }

    public static void writeLog(int entryCount,boolean success) {
        DateFormat dateForm = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date currDate = new Date();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/logfile.txt", true));
            if(success){
                out.write("\nCalAd Run Successfully at : "+dateForm.format(currDate)+" || Entries Added : "+entryCount); 
            }
            else{
                out.write("\nCalAd Run Unsuccessfully at : "+dateForm.format(currDate)+" with error : ");
                switch (entryCount) {
                    case 0:
                        System.out.println("Couldn't Connect to Google Calendar : ");
                        out.write("Couldn't Connect to Google Calendar");
                        break;
                    case 1:
                        System.out.println("Cannot Find the Data : ");
                        out.write("Cannot Find the Data");
                        break;
                    case 2:
                        System.out.println("Couldn't Add Contests : ");
                        out.write("Couldn't Add Contests");
                        break;
                    default:
                        out.write("Unknown");
                        break;
                }
            }
            out.close();
        } catch (IOException ex) {
            System.out.println("Couldn't Write Logfile : " + ex);
        }
    }
    public static void main(String... args) throws IOException, GeneralSecurityException {
        Calendar service = connectToGoogle();
        Contest.WorkingCalendar();
        Contest.getPreference();
        int changes = pushContests(service);
        writeLog(changes,true);
    }
}

//by shubh-shah
