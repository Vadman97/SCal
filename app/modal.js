// var modal           = document.getElementById('modal');
//
// var loginBtn        = document.getElementById('loginButton');
// var uploadBtn        = document.getElementById('uploadButton');
//
// // Get the <span> element that closes the modal
// var span = document.getElementsByClassName("close")[0];
// $('#modal').load("partials/loginModal.html");
//
// // When the user clicks on the button, open the modal
// loginBtn.onclick = function() {
//
//     modal.style.display = "block";
// }
//
// uploadBtn.onclick = function() {
//     // $('#modal').load("partials/uploadModal.html");
//     modal.style.display = "block";
// }
//
// // When the user clicks on <span> (x), close the modal
// span.onclick = function() {
//     modal.style.display = none;
// }
//
// // When the user clicks anywhere outside of the modal, close it
// window.onclick = function(event) {
//     if (event.target == modal) {
//         modal.style.display = "none";
//     }
// }

$(function() {
    $("#uploadButton").click(function() {
        $("#modal").load("partials/uploadModal.html");
        $("#modal").toggleClass("modal-active");
    });
    $("#loginButton").click(function() {
        $("#modal").load("partials/loginModal.html");
        $("#modal").toggleClass("modal-active");
    });
    $("#close").click(function() {
        $("#modal").removeClass("modal-active");
    });
});
