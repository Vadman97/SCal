<jsp:useBean id="user" class="user.UserData" scope="session"/>
<jsp:setProperty name="user" property="*"/> 
<HTML>
<BODY>
	
	<% if (user.write()) { %>
		You entered<BR>
		Name: <%= user.getUsername() %><BR>
		Email: <%= user.getEmail() %><BR>
		Password: <%= user.getPassword() %><BR>
	<% } else {%>
		Sorry someone with that user already exists! Try again.
	<% } %>
</BODY>
</HTML>