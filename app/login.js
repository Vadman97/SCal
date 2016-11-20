var lmodal = document.getElementById('loginModal');
var currTab = document.getElementById('login');

var submit = document.getElementById('submit');

function loginSwitchTabs(loginType) {
    
    if(loginType =='signup')
    {
        $("#signup").addClass('select');
        $("#signupbox").slideDown();
        
        currTab = document.getElementById('signup');
    }
    else
    {
        $("#signup").removeClass('select');
        $("#login").addClass('select');
        $("#signupbox").slideUp();
        $("#loginbox").slideDown();
        
        currTab = document.getElementById('login');
    }
}

function validateUser() {
    
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    
    var loginLogic = $(currTab).attr('id');

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
                    // POPULATE Calendar with Data
                    
                    getRequest();
                    
                    lmodal.style.display = "none";
                    return false;
                } else {
                    
                    alert("Invalid: Username or Password");
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
                    
                    lmodal.style.display = "none";
                    return false;
                } else {
                    
                    alert("Invalid: Username/Email already exists");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Signup");
            } 
        });
    }
}

function getRequest() {
    
    $.ajax({
            url : "/calendar?view=all",
            type: "GET",
            success: function(data)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    
                    var scope = angular.element(document.getElementById("bodyTagID")).scope();
                    
                    var userEvent;
                    
                    // more data to be added!
                    
                    for(let obj of data) {
                        
                        var Sstr = obj["event attributes"]["start_time"]
                        
                        var Sres = str.split("-")
                        var Syear = res[0]
                        var Smonth = res[1]
                        var Sday = res[2]
                        
                        var Estr = obj["event attributes"]["end_time"]
                        
                        var Eres = str.split("-")
                        var Eyear = res[0]
                        var Emonth = res[1]
                        var Eday = res[2]
                        
                        scope.events.append({id: obj["id"], title: obj["event attributes"]["name"], start: new Date(Syear, Smonth, Sday), end: new Date(Eyear, Emonth, Eday)});
                    }
                    
                    lmodal.style.display = "none";
                    return false;
                } else {
                    
                    alert("Get request not returned");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - getRequest");
            },
            dataType: json
        });
}