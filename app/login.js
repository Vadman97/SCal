var attempt = 3; // Variable to count number of attempts.
// Below function Executes on click of login button.

var lmodal = document.getElementById('loginModal');

function validateLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var formData = {username: username,password: password};

    $.ajax({
        url : "/login",
        type: "POST",
        data : formData,
        success: function(data, textStatus, jqXHR)
        {
            //data: data from server
            if(JSON.parse(data)["success"]) {
                // Redirecting to other page.
                // POPULATE Calendar with Data

                lmodal.style.display = "none";
                return false;
            } else {

            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            alert("Server Not Connected");
        }
    });
}

function validateCreate() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // Handle POST request here

    var formData = {username: username,password: password};

    $.ajax({
        url : "/create_user",
        type: "POST",
        data : formData,
        success: function(data, textStatus, jqXHR)
        {
            //data: data from server
            if(JSON.parse(data)["success"]) {
                // Redirecting to other page.
                // POPULATE Calendar with Data

                lmodal.style.display = "none";
                return false;
            } else {


            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {

        }
    });

}
