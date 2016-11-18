// friends.js

var tabsContent = document.getElementsByClassName("modal-friends-content-main-tabcontent");

tabsContent[0].style.display = "flex";
tabsContent[0].style.flexFlow = "column nowrap";

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

// friends.js EOF
