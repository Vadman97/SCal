// logout.js

function logoutUser() {
    
    $.ajax({
            url : "/user/logout",
            type: "POST",
            success: function(data, jqXHR)
            {
                window.location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Logout");
            }
    });
}