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

    console.log(startTime[0]);
    console.log(endTime[0]);

    if (startTime[0]-8 < 0) {
        startTime[0] += 23;
    } else {
        startTime[0] -= 8;
    }
    if (endTime[0]-8 < 0) {
        endTime[0] += 23;
    } else {
        endTime[0] -= 8;
    }

    $('#editStartTime').val(startTime[0] + ":" + startTime[1] + ":" + startTime[2]);
    $('#editEndTime').val(endTime[0] + ":" + endTime[1] + ":" + endTime[2]);
    $('#editStartDate').val(startDate);
    $('#editEndDate').val(endDate);
    $('#editLocation').val(event.location);
    $('#editDescription').val(event.description);

});

// editEvent.js EOF
