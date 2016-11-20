// enroll.js

var scope = angular.element($('#bodyTagID')).scope();

var emodal = document.getElementsByClassName('modal-enroll')[0];

function addClasses() {
    
    var addClass = document.getElementById("sectionID").value;
    
    var formData = {section_id: addClass};
    
    $.ajax({
            url : "/classes",
            type: "POST",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    // Add class to calendar
                    
                    return true;
                } else {

                    alert("Invalid section ID");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Add");
            }
        });
}

function deleteClasses() {
    
    var deleteClass = document.getElementById("sectionID").value;
    
    var formData = {section_id: deleteClass};
    
    var r = confirm("Are you sure you want to remove this class?");
    
    if(r == true) {
        $.ajax({
            url : "/classes",
            type: "DELETE",
            data : formData,
            success: function(data, textStatus, jqXHR)
            {
                //data: data from server
                if(JSON.parse(data)["success"]) {
                    // Add class to calendar
                    
                    return true;
                } else {

                    alert("You are not enrolled in this class");
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                alert("Server Not Connected - Remove");
            }
        });
    }
    
}