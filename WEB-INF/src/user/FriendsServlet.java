package user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.User.Friendship;
import util.Util;

public class FriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//load all friends		
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}

		JsonObject result = new JsonObject();
		JsonArray friendships = new JsonArray();
		for (Friendship f: u.getFriends()) {
			JsonObject o = new JsonObject();
			o.addProperty("username", u.equals(f.first) ? f.second.getUsername() : f.first.getUsername());
			o.addProperty("status", f.status);
			friendships.add(o);
		}
		
		result.add("friends", friendships);
		result.addProperty("success", true);
		res.getWriter().print(result.toString());
		res.getWriter().close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// add new friend
		
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}

		JsonParser jsp = new JsonParser();
		JsonObject obj = jsp.parse(Util.getRequestData(req)).getAsJsonObject();
		
		if (!obj.has("username")) {
			Util.close(res, false);
			return;
		}
		
		try {
			Connection con = Util.getConn();
			User friend = User.getUser(con, obj.get("username").getAsString());
			if (friend == null) {
				Util.close(res, false);
				return;
			}
			u.writeFriendship(con, new Friendship(Friendship.PENDING, u, friend));
			Util.close(res, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//update friend status
		
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}

		JsonParser jsp = new JsonParser();
		JsonObject obj = jsp.parse(Util.getRequestData(req)).getAsJsonObject();
		
		if (!obj.has("username") || !obj.has("status")) {
			Util.close(res, false);
			return;
		}
		
		try {
			Connection con = Util.getConn();
			User friend = User.getUser(con, obj.get("username").getAsString());
			if (friend == null) {
				Util.close(res, false);
				return;
			}
			
			for (Friendship f: u.getFriends()) {
				if (f.first.equals(u) && f.second.equals(friend)) {
					f.status = obj.get("status").getAsString();
					u.writeFriendship(con, f);
					break;
				}
			}
			
			Util.close(res, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
