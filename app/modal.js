var modal = document.getElementById('modal');

var createEventBtn = document.getElementById('createButton');
var loginBtn = document.getElementById('loginButton');
var uploadBtn = document.getElementById('uploadButton');
// var friendsBtn = document.getElementById('friendsButton');
var enrollBtn = document.getElementById('enrollButton');
var commonBtn = document.getElementById('commonButton');

var scope = angular.element($('#bodyTagID')).scope();

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

createEventBtn.onclick = function() {
	$('#modal').load("partials/createEventModal.html", closeModal);
	$('#modal').toggleClass("modal-active");
}

// When the user clicks on the button, open the modal
loginBtn.onclick = function() {
	scope.loggedIn(function() {
		$('#modal').load("partials/logoutModal.html", closeModal);
		$('#modal').toggleClass("modal-active");
	}, function() {
		$('#modal').load("partials/loginModal.html", closeModal);
		$('#modal').toggleClass("modal-active");
	});
}

uploadBtn.onclick = function() {
	$('#modal').load("partials/uploadModal.html", closeModal);
	$('#modal').toggleClass("modal-active");
	// modal.style.display = "block";
}

/* friendsBtn.onclick = function() {
	$('#modal').load("partials/friendsModal.html", closeModal);
	$('#modal').toggleClass("modal-active");
} */

enrollBtn.onclick = function() {
	$('#modal').load("partials/enrollModal.html", closeModal);
	$('#modal').toggleClass("modal-active");
}

commonBtn.onclick = function() {
	$('#modal').load("partials/commonModal.html", closeModal);
	$('#modal').toggleClass("modal-active");
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
