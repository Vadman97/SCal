// enroll.js

var scope = angular.element($('#bodyTagID')).scope();
var emodal = document.getElementsByClassName('modal-enroll')[0];

function addClasses() {

    var addClass = parseInt(document.getElementById("sectionID").value, 10);
    var formData = JSON.stringify({section_id: addClass});

    $.post("/classes", formData, function(data, status) {
        if (JSON.parse(data).success) {
            $.get("/calendar&view=all", function(data) {
                
            });
            scope.renderCalendar();
        } else {
            console.log("/classes POST error");
        }
    });

    // $.ajax({
    //         url : "/classes",
    //         type: "POST",
    //         data : formData,
    //         success: function(data, textStatus, jqXHR)
    //         {
    //             //data: data from server
    //             if(JSON.parse(data)["success"]) {
    //                 // Add class to calendar
    //                 $.get("/calendar&view=all", function(res) {
    //                     console.log(res);
    //                 });
    //                 return true;
    //             } else {
    //                 alert("Invalid section ID");
    //             }
    //         },
    //         error: function(jqXHR, textStatus, errorThrown)
    //         {
    //             alert("Server Not Connected - Add");
    //         }
    //     });
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
