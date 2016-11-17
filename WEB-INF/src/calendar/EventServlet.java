package calendar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import user.User;
import util.Util;

public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//create		
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}
		
		Event e = Event.parse(Util.getRequestData(req));
		if (e == null) {
			Util.close(res, false);
			return;
		}
		e.write(u);
		Util.close(res, true);
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//remove
		JsonObject o = Util.parseJson(req, res);
		if (o == null) {
			Util.close(res, false);
			return;
		}
		
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}
		
		if (o.has("id")) {
			Event e = new Event(o.get("id").getAsLong());
			Util.close(res, e.delete());
		}
	}
	
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//update
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}
		
		Event e = Event.update(Util.getRequestData(req));
		if (e == null) {
			Util.close(res, false);
			return;
		}
		
		e.write(u);
		Util.close(res, true);
	}
}
