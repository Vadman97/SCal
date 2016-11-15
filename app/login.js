var attempt = 3; // Variable to count number of attempts.
// Below function Executes on click of login button.

var modal = document.getElementById('loginModal');

function validateLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    
    // Handle POST request here
    
    if ( username == "Clifford" && password == "Lee"){
        console.log("Login successfully");
        
        modal.style.display = "none";
        
        var formData = {username: username,password: password};
    
        $.ajax({
            url : "/login",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
	           //data: data from server 
            },
            error: function(jqXHR, textStatus, errorThrown)
            {

            }
        });
        
        // Redirecting to other page.
        return false;
    }
    else{
        attempt --;// Decrementing by one.
        alert("You have left "+attempt+" attempt;");
        
        // Disabling fields after 3 attempts.
        if( attempt == 0){
            
        document.getElementById("username").disabled = true;
        document.getElementById("password").disabled = true;
        document.getElementById("submit").disabled = true;
            
        return false;
        }
    }
}

function validateCreate() {
    
    
}