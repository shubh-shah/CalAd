# CalAd

Adds all Competitive Coding contests directly to your Google Calendar    

### Usage:
Command Line application can be run by bgprocess.ps1(Windows)/bgprocess.sh(Linux) which can be added to Task Scheduler/cron  
or  
from the project directory: >./gradlew runBackground

GUI application (For options to customise or logout) can be run by calad.ps1(Windows)/calad.sh(Linux)  
or  
from the project directory: >./gradlew run

### Requirements:    
JDK >=11      
Needs permissions to view and edit Calenders    

Check user/logfile.txt to view any errors in case of an unexpected crash or run the commandline app for more details on the exceptions 

Data about contests taken from CoderCalendar-API by nishanthvijayan : http://api.codercalendar.io/ 
