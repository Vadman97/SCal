package user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Create extends HttpServlet {
	private static final long serialVersionUID = 5729439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		if (req.getParameterMap().containsKey("username") && req.getParameterMap().containsKey("password") 
														  && req.getParameterMap().containsKey("email")) {
			User user = new User();
			user.setUsername(req.getParameter("username"));
			user.setPassword(req.getParameter("password"));
			user.setEmail(req.getParameter("email"));
			req.getSession().setAttribute("user", user);
			
			boolean success = user.write();
			
			if (!success)
				user.clear();
			
			String json = "{\"success\": " + success + "}";
			out.println(json);
		} else {
			out.println("{\"success\": false}");
		}
		out.flush();
		out.close();
	}
}
