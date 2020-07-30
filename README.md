# CalAd

Adds all Competitive Coding contests directly to your Google Calendar

## Usage:
Command Line application can be run by bgprocess.bat(Windows)/bgprocess.sh(Linux) which can be added to Task Scheduler/cron  
or  
from the project directory: >gradle runBackground

GUI application (For options to customise or logout) can be run by calad.bat(Windows)/calad.sh(Linux)  
or  
from the project directory: >gradle run

## Requirements:    
JDK 11    
Gradle 6.5    
Needs permissions to view and edit Calenders    

Check user/logfile.txt to view any errors in case of an unexpected crash or run the commandline app for more details on the exceptions 

Data about contests taken from CoderCalendar-API by nishanthvijayan : http://api.codercalendar.io/ 
