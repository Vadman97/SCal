var app = angular.module('calendar', ['ui.calendar']);

app.controller('calendarController', function($scope) {
    $scope.eventSources = [];
    // config object
    $scope.uiConfig = {
        calendar: {
            height: 800,
            editable: true,
            header: {
                left: 'month agendaWeek agendaDay',
                center: 'title',
                right: 'today prev,next'
            },
            eventClick: $scope.alertEventOnClick,
            eventDrop: $scope.alertOnDrop,
            eventResize: $scope.alertOnResize
        }
    };
});
