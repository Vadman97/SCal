package calendar;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import user.User;
import util.Util;

public class EventShareServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// create
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}

		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("event_id") || !obj.has("target_username")) {
			Util.close(res, false);
			return;
		}

		try {
			User shareTarget = User.getUser(Util.getConn(), obj.get("target_username").getAsString());
			if (shareTarget == null) {
				Util.close(res, false);
				return;
			}
			Event e = new Event(obj.get("event_id").getAsLong());
			if (e.getId() == 0) {
				Util.close(res, false);
				return;
			}
			e.addShared(shareTarget);
			e.write(u);
			Util.close(res, true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// remove
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}

		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("event_id") || !obj.has("target_username")) {
			Util.close(res, false);
			return;
		}

		try {
			User shareTarget = User.getUser(Util.getConn(), obj.get("target_username").getAsString());
			if (shareTarget == null) {
				Util.close(res, false);
				return;
			}
			Event e = new Event(obj.get("event_id").getAsLong());
			if (e.getId() == 0) {
				Util.close(res, false);
				return;
			}
			e.removeShare(shareTarget);
			Util.close(res, true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
