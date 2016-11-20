// login.js

var scope       = angular.element($('#bodyTagID')).scope();
var lmodal      = document.getElementsByClassName('modal-login')[0];
var currTab     = document.getElementById('login');
var submit      = document.getElementById('submit');

function loginSwitchTabs(loginType) {
    if(loginType =='signup')
    {
        $("#login").removeClass('select');
        $("#signup").addClass('select');
        $("#email").css("display", "block");

        currTab = document.getElementById('signup');
    }
    else
    {
        $("#signup").removeClass('select');
        $("#login").addClass('select');
        $("#email").css("display", "none");

        currTab = document.getElementById('login');
    }
}

function validateUser() {

    var user        = document.getElementById("username").value;
    var pass        = document.getElementById("password").value;
    var email       = document.getElementById("email").value;

    var loginLogic = $(currTab).attr('id');

    if(loginLogic == 'login') {

        var formData = {username: user,password: pass};

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

                    $('#modal').toggleClass("modal-active");
                    $('#modal').html("<div></div>")

                    return true;
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

                    $('#modal').toggleClass("modal-active");
                    $('#modal').html("<div></div>")
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

                    for(var userEvent in data) {
                        scope.addEvent(data[userEvent]);
                    }

                    $('#modal').html("<div></div>")
                    $('#modal').removeClass("modal-active");

                    return false;
                } else {

                    alert("Get request not returned");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - getRequest");
            }
        });
}

// login.js EOF
