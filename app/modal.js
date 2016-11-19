// modal.js

var modal           = document.getElementById('modal');

var createEventBtn  = document.getElementById('createButton');
var loginBtn        = document.getElementById('loginButton');
var uploadBtn       = document.getElementById('uploadButton');
var friendsBtn      = document.getElementById('friendsButton');
var enrollBtn       = document.getElementById('enrollButton');
var commonBtn       = document.getElementById('commonButton');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

createEventBtn.onclick = function() {
    $('#modal').load("partials/createEventModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
    $.getScript("app/calendar.js");
}

// When the user clicks on the button, open the modal
loginBtn.onclick = function() {
    $('#modal').load("partials/loginModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
}

uploadBtn.onclick = function() {
    $('#modal').load("partials/uploadModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
}

friendsBtn.onclick = function() {
    $('#modal').load("partials/friendsModal.html", closeModal);
    $('#modal').toggleClass("modal-active");
    $.getScript("app/friendsMenu.js");
}

// When the user clicks on <span> (x), close the modal
var closeModal = function() {
    var span = document.getElementsByClassName("close")[0];
    span.onclick = function() {
        $('#modal').toggleClass("modal-active");
        $('#modal').html("<div></div>")
    }

    // When the user clicks anywhere outside of the modal, close it
}

window.onclick = function(event) {
    if (event.target == modal) {
        $('#modal').html("<div></div>")
        $('#modal').toggleClass("modal-active");
    }
}

// modal.js EOF
