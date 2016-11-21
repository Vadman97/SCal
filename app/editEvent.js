// editEvent.js

$(function() {
    var scope = angular.element($('#bodyTagID')).scope();
    var event = scope.selectedEvent;
    $('#editEventTitle').text(event.title);

    var start   = JSON.stringify(event.start).split("T");
    var end     = JSON.stringify(event.end).split("T");

    console.log(start);
    console.log(end);

    var startDate = start[0].substr(1);
    var startTime = start[1].split(":");
    startTime[2] = startTime[2].substring(0,2)

    var endDate = end[0].substr(1);
    var endTime = end[1].split(":");
    endTime[2] = endTime[2].substring(0,2)

    $('#startTime').val((startTime[0]-8) + ":" + startTime[1] + ":" + startTime[2]);
    $('#endTime').val((endTime[0]-8) + ":" + endTime[1] + ":" + endTime[2]);
    $('#startDate').val(startDate);
    $('#endDate').val(endDate);
    $('#location').val(event.location);
    $('#description').val(event.description);

});

// editEvent.js EOF
