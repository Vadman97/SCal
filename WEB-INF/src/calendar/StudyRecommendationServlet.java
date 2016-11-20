package calendar;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import user.User;
import util.Util;

public class StudyRecommendationServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;
	
	private class RecommendationJson {
		public Vector<String> users;
		public Timestamp day;
		public transient Vector<User> user_objs;
		
		public void loadUsers() {
			try {
				Connection con = Util.getConn();
				user_objs = new Vector<>();
				for (String s: users) {
					user_objs.add(User.getUser(con, s));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static RecommendationJson parse(String json) {
		RecommendationJson o = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(json, RecommendationJson.class);
		o.loadUsers();
		return o;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//load recommendations		
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}
		
		RecommendationJson r = parse(Util.getRequestData(req));
		Vector<Event> events = StudyRecommendations.findCommonTime(r.user_objs, u, r.day, req);
		
		JsonObject result = new JsonObject();
		result.add("suggestions", new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJsonTree(events));
		result.addProperty("success", true);
		res.getWriter().print(result.toString());
		res.getWriter().close();
	}
}
