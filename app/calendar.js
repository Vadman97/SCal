var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarCtrl', function($scope, $http, $timeout, uiCalendarConfig) {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();

    /* event source that contains custom events on the scope */
    $scope.events = {
      events: [
          {title: 'All Day Event',start: new Date(y, m, 1)},
          {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
          {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
          {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
          {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
      ]
    };

    /* event sources array*/
    $scope.eventSources = [$scope.events];

    /* add custom event*/
    $scope.addEvent = function(event) {
        // $scope.calendar.removeEventSource($scope.events);
        $scope.events.events.splice(0, 0, event);
        // $scope.calendar.addEventSource($scope.events);
        // console.log(uiCalendarConfig.calendars);
        $scope.renderCalendar;
    };

    /* remove event */
    $scope.remove = function(index) {
      $scope.events.splice(index,1);
      $scope.renderCalendar;
    };

    /* select event */
    $scope.selectEvent = function()
    {
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
/* EOF */
