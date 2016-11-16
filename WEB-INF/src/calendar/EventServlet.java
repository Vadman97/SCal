package calendar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.User;
import util.Util;

public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439952621174381L;
	
	public void close(HttpServletResponse res, boolean valid) {
		try {
			res.getWriter().println("{success: " + valid + "}");
			res.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private JsonObject parseJson(HttpServletRequest req, HttpServletResponse res) {
		String requestData = Util.getRequestData(req);
		return new JsonParser().parse(requestData).getAsJsonObject();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//create		
		User u = Util.getSessionUser(req);
		if (u == null) {
			close(res, false);
			return;
		}
		
		Event e = Event.parse(Util.getRequestData(req));
		if (e == null) {
			close(res, false);
			return;
		}
		e.write(u);
		close(res, true);
		
		//TODO(Vadim): share create
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//remove
		JsonObject o = parseJson(req, res);
		if (o == null) {
			close(res, false);
			return;
		}
		
		User u = Util.getSessionUser(req);
		if (u == null) {
			close(res, false);
			return;
		}
		
		if (o.has("id")) {
			Event e = new Event(o.get("id").getAsLong());
			close(res, e.delete());
		}
		
		//TODO(Vadim): share remove
	}
	
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//update
		User u = Util.getSessionUser(req);
		if (u == null) {
			close(res, false);
			return;
		}
		
		Event e = Event.update(Util.getRequestData(req));
		if (e == null) {
			close(res, false);
			return;
		}
		
		e.write(u);
		close(res, true);
	}
}
