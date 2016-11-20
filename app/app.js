// calendar.js

var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarCtrl', function($scope, $http, $timeout, $compile, uiCalendarConfig) {

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    // on page load, check if user is already logged into a prior session
    // if so, load the logged in user's events
    $http({
        method: 'GET',
        url: '/user/isLoggedIn'
    }).then(function(res) {
        if (res.success === true) {
            // TODO IF ALREADY LOGGED IN THEN POPULATE MAP AND CHANGE USER PANEL
        }
    }, function(res) {
        console.log("FAILED isLoggedIn");
        console.log(res);
    });

    // scope data
    $scope.user = "Guest";

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
      }]
    };

    /* event sources array*/
    $scope.eventSources = [$scope.events];

    /* load event onto front end */
    $scope.addEvent = function(event) {
        $scope.events.events.splice(0, 0, event);
    };

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
        eventClick: $scope.selectEvent,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize
      }
    };

});

// calendar.js EOF
