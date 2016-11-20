/* createEvent.js */

var friendSource = [
    "Clifford Lee",
    "Christopher Lee",
    "Samuel Breck",
    "Samantha Brech",
    "Gautam Paranjape",
    "Gotham Bold",
    "Vadim Korolik",
    "Vladim Kouroboros",
    "Prachi Nawathe",
    "Pranav Nowaythe"
]

var friendDatalist = document.getElementById("modal-createEvent-friends-list");
var friendInput = document.getElementById("modal-createEvent-friends-searchbar");

var title = document.getElementById("eventTitle");
var startTime = document.getElementById("startTime");
var startDate = document.getElementById("startDate");
var endTime = document.getElementById("endTime");
var endDate = document.getElementById("endDate");
var loc = document.getElementById("eventLocation");
var description = document.getElementById("eventDescription");



// FOR WHEN WE DO THE BACKEND FRIENDLIST INTEGRATION

var request = new XMLHttpRequest();

request.onreadystatechange = function(res) {
    if (request.readyState === 4) {
        if (request.status === 200 && res.target.response.success === true) {
            var jsonOptions = JSON.parse(request.responseText);

            jsonOptions.forEach(function(item) {
                if (item.status === 'accepted') {
                    var option = document.createElement('option');
                    option.value = item.username;
                    friendDatalist.appendChild(option);
                }
            });
            friendInput.placeholder = "Search through friends";
        } else {
            friendInput.placeholder = "Couldn't load friends :o(";
        }
    }
};

request.open('GET', '/friends', true);
request.send();


/* Updated AJAX requests */



/* event creation -> angular scope */




function debug() {
    var startDateArr = startDate.value.split("-");
    var endDateArr = endDate.value.split("-");
    var startTimeArr = startTime.value.split(":");
    var endTimeArr = endTime.value.split(":");

    var event = {
        "title": title.value,
        "start": new Date(startDateArr[0], startDateArr[1]-1, startDateArr[2], startTimeArr[0], startTimeArr[1]),
        "end": new Date(endDateArr[0], endDateArr[1]-1, endDateArr[2], endTimeArr[0], endTimeArr[1]),
        // "location": loc.value,
        // "description": description.value,
        // "friends": [],
        "stick": true
    };

    var scope = angular.element($('#bodyTagID')).scope();

    scope.addEvent(event);
    scope.renderCalendar();

    $('#modal').html("<div></div>")
    $('#modal').toggleClass("modal-active");
}

/* createEvent.js EOF */
