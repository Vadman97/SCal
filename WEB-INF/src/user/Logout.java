package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Logout extends HttpServlet {
	private static final long serialVersionUID = 5729439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Object o = req.getSession().getAttribute("user");
		if (o != null && o instanceof UserData) { 
			((UserData)o).clear();
		}
		res.getWriter().close();
	}
}
