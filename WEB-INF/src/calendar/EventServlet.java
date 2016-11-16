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
	private static final String[] expectedParams = {
		"name", "start_time", "end_time", "location", "description", "color", "notify"
	};
	
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
		JsonObject o = new JsonParser().parse(requestData).getAsJsonObject();
		for (String s: expectedParams) {
			if (!o.has(s)) {
				return null;
			}
		}
		return o;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//create
//		JsonObject o = parseJson(req, res);
//		if (o == null) {
//			close(res, false);
//			return;
//		}
		
		User u = Util.getSessionUser(req);
		if (u == null) {
			close(res, false);
			return;
		}
		
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Timestamp start = null, end = null;
//		try {
//			start = new java.sql.Timestamp(df.parse(o.get(expectedParams[1]).getAsString()).getTime());
//			end = new java.sql.Timestamp(df.parse(o.get(expectedParams[2]).getAsString()).getTime());
//		} catch (ParseException e) { 
//			e.printStackTrace();
//		}
//		Event e = new Event(-1, o.get(expectedParams[0]).getAsString(), 
//								start, end,
//								o.get(expectedParams[3]).getAsString(),
//								o.get(expectedParams[4]).getAsString(),
//								Event.getColor(o.get(expectedParams[5]).getAsString()),
//								o.get(expectedParams[6]).getAsBoolean());
		
		Event e = Event.parse(Util.getRequestData(req));
		if (e == null) {
			close(res, false);
			return;
		}
		e.write(u);
		close(res, true);
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
	}
	
	public void doUpdate(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//update
	}
}
