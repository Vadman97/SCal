/* createEvent.js */

// hmm?
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


// FOR WHEN WE DO THE BACKEND FRIENDLIST INTEGRATION
var request = new XMLHttpRequest();

request.onreadystatechange = function(res) {
    if (request.readyState === 4) {
        if (request.status === 200) {
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



function debug() {
    console.log(
        title.value + " " +
        startTime.value + " " +
        startDate.value + " " +
        endTime.value + " " +
        endDate.value
    );
}

/* createEvent.js EOF */
