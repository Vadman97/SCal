// editEvent.js

var name        = document.getElementById('editEventTitle');
var startDate   = document.getElementById('editStartDate');
var startTime   = document.getElementById('editStartTime');
var endDate     = document.getElementById('editEndDate');
var endTime     = document.getElementById('editEndTime');
var loc         = document.getElementById('editLocation');
var description = document.getElementById('editDescription');
var color       = document.getElementById('editColorSelect');
var scope = angular.element($('#bodyTagID')).scope();

$(function() {
    var event = scope.selectedEvent;
    $('#editEventTitle').text(event.title);


    var start   = JSON.stringify(event.start._i).split(" ");
    var startDate = start[0].split("-");
    var startTime = start[1].split(":");

    var end     = JSON.stringify(event.end._i).split(" ");
    var endDate = end[0].split("-");
    var endTime = end[1].split(":");

    $('#editEventTitle').val(event.title);
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
    $.ajax({
        url: '/event',
        type: 'PUT',
        data: JSON.stringify({
            name: $('#editEventTitle').val(),
            start_time: startDate.value + " " + startTime.value + ":00",
            end_time: endDate.value + " " + endTime.value + ":00",
            location: loc.value,
            description: description.value,
            color: color.value,
            relationship: "owned",
            notify: true,
            id: scope.selectedEvent.id
        }),
    }).done(function() {
        console.log('Event edited');
        scope.events.events.splice(0);
        scope.loadAllEvents();
        scope.renderCalendar();
    });
}

/* deletes event from the server with ajax DELETE request*/
function deleteEvent()
{
console.log(scope.selectedEvent);
}





// editEvent.js EOF
