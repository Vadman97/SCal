package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Util;

public class IsLoggedIn extends HttpServlet {
	private static final long serialVersionUID = 1729439952621174381L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn())
			Util.close(res, false);
		else
			Util.close(res, true);
	}
}
