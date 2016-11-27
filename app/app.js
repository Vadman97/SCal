// calendar.js

var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarCtrl', function($scope, $http, $timeout, $compile, uiCalendarConfig) {

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    $scope.isLoggedIn = false;


    /* event source that contains custom events on the scope */
    $scope.events = {
      events: [{title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false}]
    };

    /* event sources array*/
    $scope.eventSources = [$scope.events];

    $scope.loggedIn = function(loggedInCB, loggedOutCB) {
    	$.get("/user/isLoggedIn", function(resp) {
            if (JSON.parse(resp).success) {
            	$('#sidebarUser').html(JSON.parse(resp).username);
            	loggedInCB();
            } else
            	loggedOutCB();
        });
    }

    /* load event onto front end */
    $scope.addEvent = function(event) {
    	$scope.events.events.splice(0, 0, event)
    };

    // helper function to parse data from server format
    $scope.parseServerEvent = function(event)
	 {
	     var result = event;
	     result.title = result.name;
	     result.start = result.start_time;
	     result.end = result.end_time;
	     result.stick = "true";
	     delete result.name;
	     delete result.start_time;
	     delete result.end_time;

	     return result;
	 }

     $scope.loadAllEvents = function() {
    	 $scope.loggedIn(function () {
    		 WebsocketConnection.initialize();
            $.get("/calendar?view=all", function(data) {
                if (JSON.parse(data).success) {
                    var events = JSON.parse(data).events;

                    for (var event in events) {
                        $scope.addEvent($scope.parseServerEvent(events[event]));
                    }
                    $scope.renderCalendar();
                }
            });
    	 }, function () {
    		 //IN GUEST MODE, LOAD FROM JS SESSION
    		 if (window.localStorage) {
	   			 var events = JSON.parse(window.localStorage.getItem("events"));
	   			 if (events != null) {
	   				 for (var event of events) {
	   					$scope.addEvent($scope.parseServerEvent(event));
	   				 }
	   				$scope.renderCalendar();
	   			 }
	   		 }
    	 });
    }

    /* create event - post it to server and load onto front end */
    $scope.postEvent = function(event, cb) {
    	$scope.loggedIn(function () {
    		// HTTP POST TO SERVER
            $http({
                method: 'POST',
                url: '/event',
                data: event
            }).then(cb, function() {
                console.log("ERROR");
            });
	   	 }, function () {
	   		 //IN GUEST MODE, CACHE TO JS SESSION
	   		 if (window.localStorage) {
	   			 var events = JSON.parse(window.localStorage.getItem("events"));
	   			 if (events == null) events = [];
	   			 events.push(event);
	   			 window.localStorage.setItem("events", JSON.stringify(events));
	   		 }
	   	 });
    };

    $scope.pushAllGuestData = function() {
    	if (window.localStorage) {
  			 var events = JSON.parse(window.localStorage.getItem("events"));
  			 if (events != null) {
  				 for (var event of events) {
  					 $scope.postEvent(event, function() {});
  				 }
  				 window.localStorage.clear();
  			 }
  		 }
    }

    /* get length of events */
    $scope.getEventsLength = function()
    {
        return $scope.events.events.length;
    }

    /* remove event */
    $scope.remove = function(index) {
      $scope.events.splice(index,1);
      $scope.renderCalendar;
    };

    /* select event */
    $scope.selectEvent = function(event,jsEvent,view)
    {
        $scope.selectedEvent = event;
        $('#modal').load("partials/editEventModal.html", closeModal);
        $('#modal').toggleClass("modal-active");
    }

    /* selects day and opens create modal */
    $scope.dayClickCreateEvent = function(date, jsEvent, view)
    {
        console.log(date);
    }

    /* Change View */
    $scope.changeView = function(view,calendar) {
      uiCalendarConfig.calendars[calendar].fullCalendar('changeView',view);
    };

    /* Change View */
    $scope.renderCalendar = function(calendar) {
      $timeout(function() {
        if(uiCalendarConfig.calendars[calendar]){
          uiCalendarConfig.calendars[calendar].fullCalendar('render');
        }
      });
    };

    /* Clear Calendar */
    $scope.clearCalendar = function(calendar) {
        $scope.events.events.splice(0);
        $scope.renderCalendar;
    };

    $scope.alertOnResize = function() {
        alert("HELLO");
    };

    /* config object */
    $scope.uiConfig = {
      calendar:{
        height: window.innerHeight*0.9,
        editable: true,
        displayEventTime: true,
        timezone: "local",
        defaultEventMinutes: 60,
        header:{
          left: 'month agendaWeek agendaDay',
          center: 'title',
          right: 'today prev,next'
        },
        dayClick: $scope.dayClickCreateEvent,
        eventClick: $scope.selectEvent,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize
      }
    };

});

// calendar.js EOF
