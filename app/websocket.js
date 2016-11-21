var printer = {};

printer.log = (function(message) {
	console.log(message);
    var notifs = $('.sidebar-container-notifs');
    var heading = "New Event Shared!";
    var body = message;
    var div = '<div class="eventnotif">' +
		    + '<h3>'
		    + heading
		    + '</h3>'
			+ '<p>'
			+ body
			+ '</p>'
		    + '</div>';
    notifs.append(div);
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
        printer.log(message.data);
    };
});

WebsocketConnection.initialize = function() {
    if (window.location.protocol == 'http:') {
        WebsocketConnection.connect('ws://' + window.location.host + '/WebsocketConnection');
    } else {
        WebsocketConnection.connect('wss://' + window.location.host + '/WebsocketConnection');
    }
};

$().ready(function () {WebsocketConnection.initialize()});