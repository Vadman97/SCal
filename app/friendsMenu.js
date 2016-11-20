// friendsMenu.js

var tabsContent     = document.getElementsByClassName("modal-friends-content-main-tabcontent");
var friendsSidebar  = document.getElementsByClassName("modal-friends-content-sidebar")[0];

tabsContent[0].style.display = "flex";
tabsContent[0].style.flexFlow = "column nowrap";

$.get("/friends", function(data) {
    if (JSON.parse(data).success === true) {
        var friends = JSON.parse(data).friends;
        for (var x in friends) {
            if (friends[x].status === "accepted") {

                var div = document.createElement("div");
                div.setAttribute("class", "friend");
                div.innerHTML = '<img src="./assets/img/placeholder.jpg" alt="friend :)" class="friend-img"><h4>' + friends[x].username + '</h4>';
                friendsSidebar.append(div);

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
