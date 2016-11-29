/* createEvent.js */

var friendDatalist  = document.getElementById("modal-createEvent-friends-list");
var friendInput     = document.getElementById("modal-createEvent-friends-searchbar");

var title           = document.getElementById("eventTitle");
var startTime       = document.getElementById("startTime");
var startDate       = document.getElementById("startDate");
var endTime         = document.getElementById("endTime");
var endDate         = document.getElementById("endDate");
var color           = document.getElementById("colorSelect");
var loc             = document.getElementById("eventLocation");
var description     = document.getElementById("eventDescription");

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

/* Every time a friend is selected, add it to an array which will eventually be used if sharing event */

/* event creation -> angular scope */

function create() {
    if (!validateForm()) {
        alert("Form was not completed!");
        return false;
    }
    var startDateArr = startDate.value.split("-");
    var endDateArr = endDate.value.split("-");
    var startTimeArr = startTime.value.split(":");
    var endTimeArr = endTime.value.split(":");
    var scope = angular.element($('#bodyTagID')).scope();
    
    var event = {
        "title": title.value,
        "start": new Date(startDateArr[0], startDateArr[1]-1, startDateArr[2], startTimeArr[0], startTimeArr[1]),
        "end": new Date(endDateArr[0], endDateArr[1]-1, endDateArr[2], endTimeArr[0], endTimeArr[1]),
        "location": loc.value,
        "description": description.value,
        "color": color.value,
        "relationship": "owned",
        "friends": [],
        "stick": true
    };

    var eventToPost = {
        "name": title.value,
        "start_time": startDate.value + " " + startTime.value + ":00",
        "end_time": endDate.value + " " + endTime.value + ":00",
        "location": loc.value,
        "description": description.value,
        "color": color.value,
        "relationship": "owned",
        "notify": true
    }


    scope.postEvent(eventToPost, scope.addEvent(event));
    if (scope.selectedEvent)
    	scope.shareEvent(scope.selectedEvent.id, $(friendInput).val());
    scope.renderCalendar();

    $('#modal').html("<div></div>")
    $('#modal').toggleClass("modal-active");
}


/* form validation function */
function validateForm()
{
    if (title.value == "" || startTime.value == "" || endTime.value == "" || startDate.value == "" || endDate.value == "" || loc.value == "" || description.value == "") {
        return false;
    }
    return true;
}

/* createEvent.js EOF */
