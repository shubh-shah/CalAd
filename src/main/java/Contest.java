import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Contest {
	private static String WORKING_CALENDAR;
	public static String EXCLUDE_TAG_PATH = "user/excludeTagsUser";
	public static String WORKING_CALENDAR_PATH = "user/WorkingCalendar";
	public static int maxLength = 14;
	public static Vector<String> allPlatforms;
	public static Vector<String> exclusionList;
	//public static int userPlatforms = 0b11111110;
	public String name;
	public DateTime startTime;
	public DateTime endTime;
	public String platform, url;

	public Contest(String name, DateTime startTime, DateTime endTime, String platform, String url) {
		this.name = name.trim();
		this.startTime = startTime;
		this.endTime = endTime;
		this.platform = platform;
		this.url = url;
		allPlatforms = new Vector<String>() {
			{
				add("codechef");
				add("codeforces");
				add("hackerrank");
				add("hackerearth");
				add("topcoder");
				add("leetcode");
				add("atcoder");
				//add("kaggle");
			}
		};
	}

	public static void WorkingCalendar() {
		getWorkingCalendar();
		if (WORKING_CALENDAR == null) {
			System.out.println("Please enter the Calendar ID of Working Calendar found in -> Your Google Calendar -> Settings and Sharing -> Integration : ");
			Scanner in = new Scanner(System.in);
			setWorkingCalendar(in.nextLine());
			in.close();
		}
	}

	public static String getWorkingCalendar() {
		try {
			Scanner sc = new Scanner(new File(WORKING_CALENDAR_PATH));
			if (sc.hasNextLine()) {
				WORKING_CALENDAR = sc.nextLine();
				return WORKING_CALENDAR;
			} else
				return null;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public static void setWorkingCalendar(String Cal) {
		try {
			WORKING_CALENDAR = Cal;
			BufferedWriter out = new BufferedWriter(new FileWriter(WORKING_CALENDAR_PATH));
			out.write(WORKING_CALENDAR);
			out.close();
		} catch (IOException x) {
			System.out.println("Couldn't Save current Working Calendar");
		}
	}

	public boolean contestExists(Calendar service) throws IOException { // Make this semi-offline
		Events dupl = service.events().list(WORKING_CALENDAR).setTimeMax(endTime).setTimeMin(startTime)
				.setOrderBy("startTime").setSingleEvents(true).execute();
		List<Event> items = dupl.getItems();
		if (!items.isEmpty()) {
			for (Event event : items) {
				if (this.name.equalsIgnoreCase(event.getSummary()))
					return true;
			}
		}
		return false;
	}

	public void addEventToCalender(Calendar service) throws IOException {
		EventDateTime start = new EventDateTime().setDateTime(startTime);
		EventDateTime end = new EventDateTime().setDateTime(endTime);

		Event event = new Event().setSummary(name).setLocation(url).setDescription(platform).setStart(start)
				.setEnd(end);

		event = service.events().insert(WORKING_CALENDAR, event).execute();
	}

	public static void getPreference() {
		try {
			FileInputStream fis = new FileInputStream(Contest.EXCLUDE_TAG_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Contest.exclusionList = (Vector<String>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			Contest.setPreference();
		}
		/*try {
			Scanner sc = new Scanner(new File("src/main/resources/Preference"));
			if (sc.hasNextInt()) {
				userPlatforms = sc.nextInt();
			} else return;
			if (sc.hasNextInt()) {
				maxLength = sc.nextInt();
			} else return;
		} catch (FileNotFoundException e) {
			setPreference();
		}*/
	}

	public static void setPreference() {
		try {
			FileOutputStream fos = new FileOutputStream(Contest.EXCLUDE_TAG_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Contest.exclusionList);
			oos.close();
			fos.close();
			/*BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/Preference"));
			out.write(userPlatforms);
			out.write(" ");
			out.write(maxLength);
			out.close();*/
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public boolean isUseful() {
		String inp = this.name;
		if ((this.endTime.getValue() - this.startTime.getValue()) / (1000 * 60 * 60 * 24) > Contest.maxLength) { 
			return false;
		}
		for (String itr : Contest.allPlatforms) {
			if(this.platform.equalsIgnoreCase(itr)/* && condition*/ ){
				return true;
			}
		}
		for (String itr : Contest.exclusionList) {
			if (inp.contains(itr)) {
				return false;
			}
		}
		return false;
	}
}

//by shubh-shah
