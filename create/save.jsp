<jsp:useBean id="user" class="db.UserData" scope="session"/>
<jsp:setProperty name="user" property="*"/> 
<% user.write(); %>
<HTML>
<BODY>
	You entered<BR>
	Name: <%= user.getUsername() %><BR>
	Email: <%= user.getEmail() %><BR>
	Password: <%= user.getPassword() %><BR>
</BODY>
</HTML>