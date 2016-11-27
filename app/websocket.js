var WebsocketConnection = {};
	
WebsocketConnection.socket = null;

WebsocketConnection.alerted = new Set();

WebsocketConnection.connect = (function(host) {
	if (WebsocketConnection.socket) {
		WebsocketConnection.socket.close();
		WebsocketConnection.socket = null;
	}

	if ('WebSocket' in window) {
        WebsocketConnection.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        WebsocketConnection.socket = new MozWebSocket(host);
    } else {
    	WebsocketConnection.printer.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    WebsocketConnection.socket.onopen = function () {};

    WebsocketConnection.socket.onclose = function () {};

    WebsocketConnection.socket.onmessage = function (message) {
    	jsobj = JSON.parse(message.data);
    	if ("eventAlert" in jsobj) {
    		if (!(WebsocketConnection.alerted.has(jsobj.id))) {
	    		var notifs = $('.sidebar-container-notifs');
	    	    var div = '<div class="eventnotif"><h3>'
	    			    + "Event Alert"
	    			    + '</h3><p>'
	    				+ jsobj.name + " is coming up soon at " + jsobj.start_time
	    				+ '</p></div>';
	    	    notifs.append(div);
	    	    WebsocketConnection.alerted.add(jsobj.id);
    		}
    	} else 
    		WebsocketConnection.printer.newShare(jsobj);
    };
    
    console.log("Websocket connected!");
});

WebsocketConnection.initialize = function() {
    if (window.location.protocol == 'http:') {
        WebsocketConnection.connect('ws://' + window.location.host + '/userConnect');
    } else {
        WebsocketConnection.connect('wss://' + window.location.host + '/userConnect');
    }
};

$().ready(function() {
	var scope = angular.element($('#bodyTagID')).scope();
	
	WebsocketConnection.printer = {};
	
	WebsocketConnection.printer.log = (function(title, message, event) {
	    var notifs = $('.sidebar-container-notifs');
	    var div = '<div class="eventnotif"><h3>'
			    + title
			    + '</h3><p>'
				+ message
				+ '</p></div>';
	    notifs.append(div);
	    scope.addEvent(scope.parseServerEvent(event));
	    scope.renderCalendar();
	});
	
	WebsocketConnection.printer.newShare = (function(json) {
		WebsocketConnection.printer.log("New Event Shared!", "Shared from " + json.notification.username, json.event);
	});
});