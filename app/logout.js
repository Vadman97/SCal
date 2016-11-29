// logout.js

function logoutUser() {

    $.ajax({
            url : "/user/logout",
            type: "POST",
            success: function(data, jqXHR)
            {
                window.location.reload();
                $('#modal').html("<div></div>")
                $('#modal').toggleClass("modal-active");
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Logout");
            }
    });
}
