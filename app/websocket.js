var scope = angular.element($('#bodyTagID')).scope();

var printer = {};

printer.log = (function(title, message, event) {
    var notifs = $('.sidebar-container-notifs');
    var div = '<div class="eventnotif"><h3>'
		    + title
		    + '</h3><p>'
			+ message
			+ '</p></div>';
    notifs.append(div);
    scope.addEvent(parseServerEvent(event));
    scope.renderCalendar();
});

printer.newShare = (function(json) {
    printer.log("New Event Shared!", "Shared from " + json.notification.username, json.event);
});

var WebsocketConnection = {};

WebsocketConnection.socket = null;

WebsocketConnection.connect = (function(host) {
    if ('WebSocket' in window) {
        WebsocketConnection.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        WebsocketConnection.socket = new MozWebSocket(host);
    } else {
        printer.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    WebsocketConnection.socket.onopen = function () {};

    WebsocketConnection.socket.onclose = function () {};

    WebsocketConnection.socket.onmessage = function (message) {
        printer.newShare(JSON.parse(message.data));
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