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

/* FOR WHEN WE DO THE BACKEND FRIENDLIST INTEGRATION
var request = new XMLHttpRequest();

request.onreadystatechange = function(res) {
    if (request.readyState === 4) {
        if (request.status === 200) {
            var jsonOptions = JSON.parse(request.responseText);

            jsonOptions.forEach(function(item) {
                var option = document.createElement('option');
                option.value = item;
                friendDatalist.appendChild(option);
            });

            input.placeholder = "Search through friends";
        } else {
            input.placeholder = "Couldn't load friends :o(";
        }
    }
};
*/

friendSource.forEach(function(friend) {
    var option = document.createElement('option');
    option.value = friend;
    friendDatalist.appendChild(option);
});

/* createEvent.js EOF */
