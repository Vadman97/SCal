var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarCtrl', function($scope, $compile, $timeout, uiCalendarConfig) {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    /* event source that pulls from google.com */
    $scope.eventSource = {
            url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic",
            className: 'gcal-event',           // an option!
            currentTimezone: 'America/Chicago' // an option!
    };

    /* event source that contains custom events on the scope */
    $scope.events = [
      {title: 'All Day Event',start: new Date(y, m, 1)},
      {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
      {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
      {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
      {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
      {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
    ];

    /* event source with custom events and cool colors :) */
    $scope.calEventsExt = {
       color: '#f00',
       textColor: 'yellow',
       events: [
          {type:'party',title: 'Lunch',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
          {type:'party',title: 'Lunch 2',start: new Date(y, m, d, 12, 0),end: new Date(y, m, d, 14, 0),allDay: false},
          {type:'party',title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
        ]
    };

    /* event source that calls a function on every view switch */
    $scope.eventsF = function (start, end, timezone, callback) {
      var s = new Date(start).getTime() / 1000;
      var e = new Date(end).getTime() / 1000;
      var m = new Date(start).getMonth();
      var events = [{title: 'Feed Me ' + m,start: s + (50000),end: s + (100000),allDay: false, className: ['customFeed']}];
      callback(events);
    };

    /* alert on eventClick */
    $scope.alertOnEventClick = function( date, jsEvent, view){
        $scope.alertMessage = (date.title + ' was clicked ');
    };
    /* alert on Drop */
     $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){
       $scope.alertMessage = ('Event Dropped to make dayDelta ' + delta);
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){
       $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
    };
    /* add and removes an event source of choice */
    $scope.addRemoveEventSource = function(sources,source) {
      var canAdd = 0;
      angular.forEach(sources,function(value, key){
        if(sources[key] === source){
          sources.splice(key,1);
          canAdd = 1;
        }
      });
      if(canAdd === 0){
        sources.push(source);
      }
    };
    /* add custom event*/
    $scope.addEvent = function() {
      $scope.events.push({
        title: 'Open Sesame',
        start: new Date(y, m, 28),
        end: new Date(y, m, 29),
        className: ['openSesame']
      });
    };
    /* remove event */
    $scope.remove = function(index) {
      $scope.events.splice(index,1);
    };
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
     /* Render Tooltip */
    $scope.eventRender = function( event, element, view ) {
        element.attr({'tooltip': event.title,
                      'tooltip-append-to-body': true});
        $compile(element)($scope);
    };

    /* config object */
    $scope.uiConfig = {
      calendar:{
        height: window.innerHeight*0.9,
        editable: true,
        header:{
          left: 'month agendaWeek agendaDay',
          center: 'title',
          right: 'today prev,next'
        },
        eventClick: $scope.alertOnEventClick,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize,
        eventRender: $scope.eventRender
      }
    };

    /* event sources array*/
    $scope.eventSources = [$scope.events, $scope.eventSource, $scope.eventsF];
    $scope.eventSources2 = [$scope.calEventsExt, $scope.eventsF, $scope.events];
});
/* EOF */

var lmodal = document.getElementById('loginModal');
var currTab = document.getElementById('login');

var submit = document.getElementById('submit');

function loginSwitchTabs(loginType) {
    
    if(loginType =='signup')
    {
        $("#signup").addClass('select');
        $("#signupbox").slideDown();
        
        currTab = document.getElementById('signup');
    }
    else
    {
        $("#signup").removeClass('select');
        $("#login").addClass('select');
        $("#signupbox").slideUp();
        $("#loginbox").slideDown();
        
        currTab = document.getElementById('login');
    }
}

function validateUser() {
    
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    
    var loginLogic = $(currTab).attr('id');

    if(loginLogic == 'login') {
        
        var formData = {username: username,password: password};
        
        $.ajax({
            url : "/user/login",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    // POPULATE Calendar with Data
                    
                    lmodal.style.display = "none";
                    return false;
                } else {
                    
                    alert("Invalid: Username or Password");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Login");
            }
        });
        
    } else {
        
        var formData = {username: username,password: password, email: email  };
        
        var formData = {username: username,password: password};
        
         $.ajax({
            url : "/user/create",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    
                    lmodal.style.display = "none";
                    return false;
                } else {
                    
                    alert("Invalid: Username/Email already exists");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Signup");
            } 
        });
    }
}
var modal           = document.getElementById('modal');

var loginBtn        = document.getElementById('loginButton');
var uploadBtn       = document.getElementById('uploadButton');
var friendsBtn      = document.getElementById('friendsButton');
var enrollBtn       = document.getElementById('enrollButton');
var commonBtn       = document.getElementById('commonButton');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on the button, open the modal
loginBtn.onclick = function() {
    $('#modal').load("partials/loginModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
}

uploadBtn.onclick = function() {
    $('#modal').load("partials/uploadModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
    // modal.style.display = "block";
}

friendsBtn.onclick = function() {
    $('#modal').load("partials/friendsModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
    $.getScript("app/friendsMenu.js");
}

// When the user clicks on <span> (x), close the modal
var closeModal = function() {
    var span = document.getElementsByClassName("close")[0];
    span.onclick = function() {
        $('#modal').toggleClass("modal-active");
        $('#modal').html("<div></div>")
    }

    // When the user clicks anywhere outside of the modal, close it
}

window.onclick = function(event) {
    if (event.target == modal) {
        $('#modal').html("<div></div>")
        $('#modal').toggleClass("modal-active");
    }
}