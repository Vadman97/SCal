var lmodal = document.getElementById('loginModal');
var currTab = document.getElementByID('login');

var submit = document.getElementById('submit');

function loginSwitchTabs() {
    
    var X=$(this).attr('id');
    
    if(X=='signup')
    {
        $("#signup").addClass('select');
        $("#signupbox").slideDown();
        
        currTab = document.getElementsByClassName('singup');
    }
    else
    {
        $("#signup").removeClass('select');
        $("#login").addClass('select');
        $("#signupbox").slideUp();
        $("#loginbox").slideDown();
        
        currTab = document.getElementsByClassName('login');
    }
}

function validateUser() {
    
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    
    var loginLogic = currTab.attr('id');

    if(loginLogic == 'login') {
        
        var formData = {username: username,password: password};
        
        $.ajax({
            url : "/user/login",
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
                alert("Server Not Connected - Login");
            }
        });
        
    } else {
        
        var formData = {username: username,password: password, email: email  };
        
        var formData = {username: username,password: password};
        
         $.ajax({
            url : "/user/create",
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
                alert("Server Not Connected - Signup");
            } 
        });
    }
}