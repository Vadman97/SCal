// editEvent.js

var name            = document.getElementById('editEventTitle');
var startDate       = document.getElementById('editStartDate');
var startTime       = document.getElementById('editStartTime');
var endDate         = document.getElementById('editEndDate');
var endTime         = document.getElementById('editEndTime');
var loc             = document.getElementById('editLocation');
var description     = document.getElementById('editDescription');
var color           = document.getElementById('editColorSelect');
var friendDatalist  = document.getElementById("modal-editEvent-friends-list");
var friendInput     = document.getElementById("modal-editEvent-friends-searchbar");
var scope = angular.element($('#bodyTagID')).scope();

/* populate friends data list for event creation/sharing */
$(function() {
    var none = document.createElement('option');
    none.value = "None";
    friendDatalist.append(none);
});

$.get("/friends", function(data) {
    if (JSON.parse(data).success === true) {
        var friends = JSON.parse(data).friends;
        for (var x in friends) {
            if (friends[x].status === "accepted") {
                var option = document.createElement('option');
                option.value = friends[x].username;
                friendDatalist.appendChild(option);
            }
        }
    }
});

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
	obj =  {
        name: $('#editEventTitle').val(),
        start_time: startDate.value + " " + startTime.value + ":00",
        end_time: endDate.value + " " + endTime.value + ":00",
        location: loc.value,
        description: description.value,
        color: color.value,
        relationship: "owned",
        notify: true,
        id: scope.selectedEvent.id
    };
	scope.loggedIn(function () {
		// LOGGED IN
		$.ajax({
	        url: '/event',
	        type: 'PUT',
	        data: JSON.stringify(obj)
	    }).done(function() {
	        console.log('Event edited');
	        scope.shareEvent(scope.selectedEvent.id, $(friendInput).val());
	        scope.loadAllEvents();
	        $('#modal').toggleClass("modal-active");
			$('#modal').html("<div></div>")
	    });
   	 }, function () {
   		 //IN GUEST MODE
//   		 if (window.localStorage) {
//   			 var events = JSON.parse(window.localStorage.getItem("events"));
//   			 if (events == null) events = [];
//   			 events.push(event);
//   			 window.localStorage.setItem("events", JSON.stringify(events));
//   		 }
   	 });
}

/* deletes event from the server with ajax DELETE request*/
function deleteEvent()
{	
	scope.loggedIn(function () {
		// LOGGED IN
		$.ajax({
	        url: '/event',
	        type: 'DELETE',
	        data: JSON.stringify({
	            id: scope.selectedEvent.id
	        })
	    }).done(function() {
	        console.log('Event deleted');
	        scope.loadAllEvents();
	        $('#modal').html("<div></div>")
	        $('#modal').toggleClass("modal-active");
	    });
   	 }, function () {
   		 //IN GUEST MODE
//   		 if (window.localStorage) {
//   			 var events = JSON.parse(window.localStorage.getItem("events"));
//   			 if (events == null) events = [];
//   			 events.push(event);
//   			 window.localStorage.setItem("events", JSON.stringify(events));
//   		 }
   	 });
}





// editEvent.js EOF
