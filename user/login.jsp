<jsp:useBean id="user" class="db.UserData" scope="session"/>
<HTML>
<BODY>
<<<<<<< HEAD
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
=======
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
		<%
	} else {
		out.println(showForm()); 
	}
}
%>
>>>>>>> 81dcb50afd1f68eb1d8c4866d9a405a0f7294c07
</BODY>
</HTML>