<jsp:useBean id="user" class="db.UserData" scope="session"/>
<HTML>
<BODY>
<% if (request.getMethod().equals("POST")) { %> 
	<% 
		user = new db.UserData();
		user.login(request.getParameter("username"), request.getParameter("password")); 
	%>

	Username: <%= user.getUsername() %><BR>
	Email: <%= user.getEmail() %><BR>
	Password: <%= user.getPassword() %><BR>
<% } else { %>
	<FORM METHOD=POST ACTION="login.jsp">
	Username: <INPUT TYPE=text NAME=username SIZE=30><BR>
	Password: <INPUT TYPE=password NAME=password SIZE=30>
	<P><INPUT TYPE=SUBMIT>
	</FORM>
<% } %>
</BODY>
</HTML>