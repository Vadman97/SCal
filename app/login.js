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
                $('#sidebarUser').html(formData.username);

                if(JSON.parse(data)["success"]) {
                	WebsocketConnection.initialize();
                	loadAllEvents();

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

function loadAllEvents() {

    $.ajax({
            url : "/calendar?view=all",
            type: "GET",
            success: function(data)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {

                    var events = JSON.parse(data)["events"];

                    for (var event in events)
                    {
                        scope.addEvent(parseServerEvent(events[event]));
                    }

                    $('#modal').html("<div></div>")
                    $('#modal').removeClass("modal-active");

                    scope.renderCalendar();

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

// helper function to parse data from server format
function parseServerEvent(event)
{
    var result = event;
    result.title = result.name;
    result.start = result.start_time;
    result.end = result.end_time;
    result.stick = "true";
    delete result.name;
    delete result.start_time;
    delete result.end_time;

    return result;
}

// login.js EOF
