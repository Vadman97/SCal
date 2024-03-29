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
        $("#submit").val("Sign Up");

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
    var em          = document.getElementById("email").value;

    var loginLogic = $(currTab).attr('id');

    if(loginLogic == 'login') {

        if (user == "" || pass == "") {
            alert("Missing Login Fields!");
            return false;
        }

        var formData = "username="+user+"&password="+pass;

        $.ajax({
            url : "/user/login",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                if(JSON.parse(data)["success"]) {
                	$('#sidebarUser').html(user);
                	
                	scope.pushAllGuestData();
                	scope.loadAllEvents();

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

        if (user == "" || pass == "" || (em == "" || !em.includes('@'))) {
            alert("Missing Sign Up Fields!");
            return false;
        }

        var formData = "username="+user+"&password="+pass+"&email="+em;
        console.log(formData);
         $.ajax({
            url : "/user/create",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                console.log(data);
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    $('#sidebarUser').html(user);
                    
                	scope.pushAllGuestData();
                	scope.loadAllEvents();

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
                        scope.addEvent(scope.parseServerEvent(events[event]));
                    }

                    $('#modal').html("<div></div>")
                    $('#modal').removeClass("modal-active");
                    
                    $('#sidebarUser').html(user);
                    scope.isLoggedIn = true;
                    
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

// login.js EOF
