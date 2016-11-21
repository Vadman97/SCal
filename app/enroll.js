// enroll.js

var scope = angular.element($('#bodyTagID')).scope();
var emodal = document.getElementsByClassName('modal-enroll')[0];

function addClasses() {

    var addClass = parseInt(document.getElementById("sectionID").value, 10);
    var formData = JSON.stringify({section_id: addClass});

    $.post("/classes", formData, function(data, status) {
        if (JSON.parse(data).success) {
            loadAllEvents();
        } else {
            console.log("/classes POST error");
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

function loadAllEvents() {
    $.get("/calendar?view=all", function(data) {
        if (JSON.parse(data).success) {
            var events = JSON.parse(data).events;
            scope.clearCalendar();
            for (var event in events) {
                scope.addEvent(scope.parseServerEvent(events[event]));
            }
            $('#modal').html("<div></div>");
            $('#modal').removeClass("modal-active");
            scope.renderCalendar();

            return true;
        } else {
            console.log("GET Request failed");
            return false;
        }
    });
}
