package user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 5729439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		if (req.getParameterMap().containsKey("username") && req.getParameterMap().containsKey("password")) {
			if (req.getSession().getAttribute("user") != null && ((User)req.getSession().getAttribute("user")).isLoggedIn())
				out.println("{\"success\": true}");
			else {
				User user = new User();
				req.getSession().setAttribute("user", user);
				String json = "{\"success\": ";
				json += user.login(req.getParameter("username"), req.getParameter("password")); 
				json += "}";
				out.println(json);
			}
		} else {
			out.println("{\"success\": false}");
		}
		out.close();
	}
}
