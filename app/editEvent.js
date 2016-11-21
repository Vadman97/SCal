// editEvent.js

$(function() {
    var scope = angular.element($('#bodyTagID')).scope();
    var event = scope.selectedEvent;
    $('#editEventTitle').text(event.title);


    var start   = JSON.stringify(event.start._i).split(" ");
    var startDate = start[0].split("-");
    var startTime = start[1].split(":");

    var end     = JSON.stringify(event.end._i).split(" ");
    var endDate = end[0].split("-");
    var endTime = end[1].split(":");

    $('#editStartTime').val(startTime[0] + ":" + startTime[1] + ":" + startTime[2].substr(0,2));
    $('#editEndTime').val(endTime[0] + ":" + endTime[1] + ":" + endTime[2].substr(0,2));
    $('#editStartDate').val(startDate[0].substr(1) + "-" + startDate[1] + "-" + startDate[2]);
    $('#editEndDate').val(endDate[0].substr(1) + "-" + endDate[1] + "-" + startDate[2]);
    $('#editLocation').val(event.location);
    $('#editDescription').val(event.description);
});

/* updates event on the server with ajax PUT request*/
function updateEvent()
{

}

/* deletes event from the server with ajax DELETE request*/
function deleteEvent()
{

}





// editEvent.js EOF
