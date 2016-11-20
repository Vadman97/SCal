<jsp:useBean id="user" class="user.User" scope="session"/>
<HTML>
<HEAD>
	<script type="application/javascript">
        var UserConnect = {};

        UserConnect.socket = null;

        UserConnect.connect = (function(host) {
            if ('WebSocket' in window) {
                UserConnect.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                UserConnect.socket = new MozWebSocket(host);
            } else {
                Z.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            UserConnect.socket.onopen = function () {};

            UserConnect.socket.onclose = function () {};

            UserConnect.socket.onmessage = function (message) {
                printer.log(message.data);
            };
        });

        UserConnect.initialize = function() {
            if (window.location.protocol == 'http:') {
                UserConnect.connect('ws://' + window.location.host + '/userConnect');
            } else {
                UserConnect.connect('wss://' + window.location.host + '/userConnect');
            }
        };
        
        var printer = {};

        printer.log = (function(message) {
        	//console.log(message);
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
        });

        UserConnect.initialize();
    </script>
</HEAD>
<BODY>
<%!
	public String showForm() {
		return "<FORM METHOD=POST ACTION='login.jsp'>" + 
				"Username: <INPUT TYPE=text NAME=username SIZE=30><BR>" +
				"Password: <INPUT TYPE=password NAME=password SIZE=30>" +
				"<P><INPUT TYPE=SUBMIT></FORM>";
	}
%>
<% if (request.getMethod().equals("POST")) { %> 
	<% if (user.login(request.getParameter("username"), request.getParameter("password"))) { %>
		Valid Login! <BR>
		Username: <%= user.getUsername() %><BR>
		Email: <%= user.getEmail() %><BR>
		Password: <%= user.getPassword() %><BR>	
		Session: <%= request.getSession().getId() %><BR>	
		<div id="console"></div>
	<% } else { 
		out.println("Invalid Login! <br>");
		out.println(showForm()); 
	}%>
<% } else {
	if (user.isLoggedIn()) {
		%> Already logged in! <BR>
		Username: <%= user.getUsername() %><BR>
		Email: <%= user.getEmail() %><BR>
		Password: <%= user.getPassword() %><BR>	
		Session: <%= request.getSession().getId() %><BR>
		<div id="console"></div>	
		<%
	} else {
		out.println(showForm()); 
	}
}
%>
</BODY>
</HTML>