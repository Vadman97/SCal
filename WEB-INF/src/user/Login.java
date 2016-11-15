package user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.UserData;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 5729439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		if (req.getParameterMap().containsKey("username") && req.getParameterMap().containsKey("password")) {
			UserData user = new UserData();
			req.getSession().setAttribute("user", user);
			String json = "{success: ";
			json += user.login(req.getParameter("username"), req.getParameter("password")); 
			json += "}";
			out.println(json);
		} else {
			out.println("{success: false}");
		}
		out.close();
	}
}
