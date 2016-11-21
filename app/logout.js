// logout.js

function logoutUser() {
    
    $.ajax({
            url : "/user/logout",
            type: "POST",
            success: function(textStatus, jqXHR)
            {
                $('#sidebarUser').html(user);

                if(JSON.parse(data)["success"]) {
                    
                     alert("Successfully logged out user");
                    
                } else {

                    alert("Error - could logout user");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Logout");
            }
    });
}