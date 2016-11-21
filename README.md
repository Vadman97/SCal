# CS 201 Final Project - USCalendar

- Samuel Breck breck@usc.edu 29909
- Prachi Nawathe pnawathe@usc.edu 29909
- Clifford Lee leecliff@usc.edu 29909
- Gautam Paranjape gparanja@usc.edu 29909
- Vadim Korolik korolik@usc.edu 29909

# Features

### See updated docs/DetailedDocumentDesign.pdf for more info.

* Calendar
	* GUI: The calendar will appear on the center of the screen with a layout similar to Google calendar, customized from the fullcalendar.io visualization library. There will be other components on the sides of the screen and a bar at the top of the page to help with navigating to other features of the calendar.

  * Changing/Saving:
    * Backend: All changes to the calendar should be automatically saved shortly after the most recent edit. The data will be sent as a HTTP PUSH event and the server will update the database accordingly. (4 hrs)

  * Sharing:
    * Frontend: A message will pop up asking if the user wants to share the event with a friend, with a text field to input the friends email address. ~~There will be a confirmation and cancel button.~~ (2 hrs)
    * Backend: The share request will be sent as a HTTP PUSH event and the server will update the database accordingly. (2 hrs)

  * Importing:
    * Frontend: There will be a button that will pop open a modal. From that modal, the user can select a file of the appropriate type (~~.pdf~~/.ics) to parse and import events. (2 hrs)
    * Backend: The selected file will be uploaded to the server and parsed using our parsers described below, and will update the user’s calendar database accordingly. (4 hrs)


* Click and Drag Events:
	* Frontend: Events on the calendar can be interacted with in a number of ways. The user can click and drag on empty stretches of the calendar to create a new event that will span that amount of time throughout the day. The user can also drag the edges of pre-existing events to alter the time span of it. Users can also move the whole event by clicking in the center of the event and dragging the entire event.(7.5 hrs)
	* Backend: The events will be saved on the server as discussed above.


* Account Management
  * User Account:
  	* Frontend: There will be buttons to log in or to create an account. Clicking either will open a modal with the appropriate forms. The user will have to provide a username, password, and email. On logging in, all of the user’s events will be populated on the calendar. (2 hrs)
  	* Backend: We’ll store the hashed representation of the user’s password in our user databases. Creating an account will add a new entry to our user table. Logging in will verify that the hash of the entered password matches the hashed, stored password. (4 hrs)
  * Guest Account:
    * Frontend: When a user would like to use the calendar without making an account, they will be able to a limited set of features of a full account and their calendar will be saved in the browser cached. (2 hrs)


* Real Time Notifications
  * Frontend: There will be a pop up on the side of the screen that will occur if an event was just shared with the user or if an assignment due date is approaching. The user will be able to close the notification, and it will not be intrusive. (1 hr)
  * Backend: Certain events on the server such as a user sharing an event with another will trigger real time notifications. If the user that should receive a notification is connected to the server with a websocket connection, the server will send a message about the event over the websocket connection for the target client’s frontend to display. ~~Alternatively the event will be broadcasted as a global websocket message and the individual frontends will figure out if the message pertains to them.~~ (8 hrs)


* ~~Email Notifications~~
  * ~~Frontend: Occasionally, users will receive updates on shared events or event notifications over the email in addition to the normal real-time notifications. These will provide daily/weekly digest of events and allow the user to respond to event share requests through a link. (3 hrs)~~
  * ~~Backend: The server will periodically calculate what users need to receive email notifications based on when they were last logged in and will send those emails directly to the users. (3 hrs)~~


* Study Recommendations
  * Frontend: The calendar will display greyed out blocks where the user could possibly schedule in some additional study time. There should be a maximum of two present on the calendar. For all of these blocks, there should be buttons to either accept, decline, or edit. (2.5 hrs)
  * Backend: Our application will periodically process the user’s schedule and return possible times for the user to add additional study times. This will be based on finding other users in the same classes with similar availabilities in their schedule. The largest blocks of time available (during working times or potentially based on nighttime preferences of users) will be saved in the database and accessible from the client, and the frontend will present these as a separate block on the schedule. (8 hrs)

* ~~PDF Syllabus Reader:~~
  * ~~Backend: The syllabi uploaded via the import feature on the frontend will be parsed to figure out key information about a class from the PDF using the Apache PDFBox library. Relevant information such as midterm/final dates, professor name, location, and class dates/times will be extracted, and the event created on the front end. (6 hrs)~~


* ICS Reader:
  * Backend: ICS files are formatted to make parsing calendar events much easier. We’ll iterate through the file and retrieve all the events listed. We’ll parse events with the iCal .ics parsing library. (4 hrs)


# Configuring the web server

## Instructions to setting up the MySQL server:
Specific instructions for your operating system can be found at https://dev.mysql.com/downloads/installer/
Instructions for Ubuntu Linux 16.04 LTS:
`sudo apt update mysql-server`
Follow configuration instructions.
Run the .sql script to configure tables, found on our repository.
## Instructions to setting up the backend:
Install tomcat8 for your operating system: https://www.ntu.edu.sg/home/ehchua/programming/howto/Tomcat_HowTo.html
Once you find the tomcat directory, in the webapps folder remove the ROOT folder and instead clone our repo: `git clone git@github.com:CSCI201-fall2016/fp_team_04.git ROOT`
The tomcat directory is the “CATALINA_HOME” directory,  different for different operating system.
Note: META-INF/context.xml may need to be configured for appropriate database access.
You need to configure catalina to work with mysql. Run `sudo chmod -R 777 /usr/share/tomcat8/lib; ./update.sh` to configure tomcat for our project.
Once you have that running you should be able to acceess the server locally at: http://localhost:8080
This deployment information is for testing and running code on your own machine. We have also configured a server to run the website permanently on a machine for easy deployment for demoing. The VM is constantly running and updates from our master branch every 15 minutes (accessable at http://scal.tech), recompiling the java source code for and redeploys the servlets. However, we test our changes in our individual environments before merging to master to make sure our deploy server is always stable.
### Configuring Eclipse for development of the web server:
Import the Eclipse project as an existing project into your workspace once you’ve cloned it.
Configure the build path to add the appropriate external java libraries. In eclipse, add following external JARs: mysql-connector, all of the JARS in the “CATALINA_BASE” lib directory. This will allow eclipse to automatically build the java code into classes.
## Installing Front End Dependencies:
Running `./update.sh` configures frontend dependencies automatically.

#### Dependencies
1. FullCalendar
2. AngularJS
3. jQuery
4. Moment.js
