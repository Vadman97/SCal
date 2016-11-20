// friendsMenu.js

var tabsContent     = document.getElementsByClassName("modal-friends-content-main-tabcontent");

tabsContent[0].style.display = "flex";
tabsContent[0].style.flexFlow = "column nowrap";

// POPULATE FRIENDS LIST ON MODAL LOAD
$.get("/friends", function(data) {
    if (JSON.parse(data).success === true) {
        var friends = JSON.parse(data).friends;
        console.log(data);
        console.log(friends);
        for (var x in friends) {
            if (friends[x].status === "accepted") {

                var div = document.createElement("div");
                div.setAttribute("class", "friend");
                console.log(friends[x].username);
                div.innerHTML = '<img src="./assets/img/placeholder.jpg" alt="friend :)" class="friend-img"><h4>' + friends[x].username + '</h4>';
                console.log($('#friends-sidebar'));
                console.log("appended");

            }
        }
    }
});

function openFriendsTab(event, tabID)
{
    var tabsClicked = document.getElementsByClassName("modal-friends-content-main-tablinks");
    var tabsContent = document.getElementsByClassName("modal-friends-content-main-tabcontent");

    for (var i=0; i<3; i++) { // <3
        var tabContent = tabsContent[i];
        var tabClicked = tabsClicked[i];
        if (tabContent.id != tabID) {
            tabContent.style.display = "none";
            tabClicked.style.backgroundColor = "green";
        } else {
            tabContent.style.display = "block";
            tabClicked.style.backgroundColor = "lightblue";
        }
    }

}

// friendsMenu.js EOF
