// enroll.js

var scope = angular.element($('#bodyTagID')).scope();
var modal = $('.modal-commonFind');

function findCommon(smart) {
	$.get("/user/isLoggedIn", function(resp) {
        if (JSON.parse(resp).success) {
        	var users = [JSON.parse(resp).username];
            for (div of $('.modal-commonFind-content-fieldgroup')[0].children) {
            	if ($(div)[0].value.length)
            		users.push($(div)[0].value);
            }
            
            var d = new Date(); 
            var date = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
            var formData = JSON.stringify({users: users, day: date, smart: smart});

            $.post("/recommendations", formData, function(data, status) {
            	data = JSON.parse(data);
                if (data.success) {
                    console.log(data);
                    if (data.suggestions) {
                		var notifs = $('.sidebar-container-notifs');
                		var div;
                    	if (smart) {
                    		for (cl in data.suggestions.events) {
                    			var users = "";
                    			for (user of data.suggestions.users)
                    				users += user.username + " ";
                    			
                        		div = '<div class="eventnotif"><h3>'
                    			    + "Study for " + cl
                    			    + '</h3><p>'
                    				+ "You should study this week with " + users
                    				+ '</p></div>';
                        	    notifs.append(div);
                    			for (event of data.suggestions.events[cl])
                    				scope.postEvent(event, scope.addEvent(scope.parseServerEvent(event)));
                    			
                    		}
                    	} else {
                			div = '<div class="eventnotif"><h3>'
	                			    + "Study recommendations added!"
	                			    + '</h3><p>'
	                				+ "You have " + data.suggestions.length + " times this week to work together."
	                				+ '</p></div>';
                    	    notifs.append(div);
	                    	for (event of data.suggestions)
	                    	    scope.postEvent(event, scope.addEvent(scope.parseServerEvent(event)));
	                    	
                    	}
                	    scope.renderCalendar();
                    }
                } else {
                    console.log("Recommendations failed!");
                }
            });
        }
    });
};