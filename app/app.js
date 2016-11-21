// calendar.js

var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarCtrl', function($scope, $http, $timeout, $compile, uiCalendarConfig) {

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    /* event source that contains custom events on the scope */
    $scope.events = {
      events: [{
          "id": 201,
          "title": "Meme hard or die trying",
          "start": new Date(y,m,d+1,10,30),
          "end": new Date(y,m,d+1,14,30),
          "location": "Internet!!!",
          "description": "Fuck up these memes doe",
          "color": "red",
          "relationship": "owned",
          "notify": true
      }, {
          "id": 202,
          "title": "We\'re all sinners all of us",
          "start": new Date(y,m,d+4,06,30),
          "end": new Date(y,m,d+4,07,30),
          "location": "Internet!!!",
          "description": "Fuck up these memes doe",
          "color": "red",
          "relationship": "owned",
          "notify": true
      }]
    };

    /* event sources array*/
    $scope.eventSources = [$scope.events];

    /* load event onto front end */
    $scope.addEvent = function(event) {
        $scope.events.events.splice(0, 0, event);
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

    /* create event - post it to server and load onto front end */
    $scope.postEvent = function(event, cb) {
        // HTTP POST TO SERVER
        $http({
            method: 'POST',
            url: '/event',
            data: event
        }).then(cb, function() {
            console.log("ERROR");
        });
    };

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
