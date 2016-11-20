package calendar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import user.User;
import util.Util;

public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//load notifications		
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}

		JsonObject result = new JsonObject();
		result.add("notifications", new Gson().toJsonTree(Notification.loadAll(u, true)));
		result.addProperty("success", true);
		res.getWriter().print(result.toString());
		res.getWriter().close();
	}
}
