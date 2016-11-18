package calendar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.User;
import util.Util;

public class CalendarServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//load calendar		
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}
		
		String viewStr = req.getParameter("view"), dayStr = null;
		
		if (viewStr != null) {
			if (!viewStr.equals("all") && req.getParameter("day") == null) {
				Util.close(res, false);
				return;
			} else if (req.getParameter("day") != null)
				dayStr = req.getParameter("day").toLowerCase();
		} else {
			Util.close(res, false);
			return;
		}
		
		Calendar c = new Calendar(u);

		JsonObject result = new JsonObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date day = dayStr != null ? sdf.parse(dayStr) : null;
			if (viewStr.equals("week"))
				result = new JsonParser().parse(c.getWeekEvents(req, day)).getAsJsonObject();
			else if (viewStr.equals("month"))
				result = new JsonParser().parse(c.getMonthEvents(req, day)).getAsJsonObject();
			else if (viewStr.equals("day"))
				result = new JsonParser().parse(c.getDayEvents(req, day)).getAsJsonObject();
			else
				result = new JsonParser().parse(c.getAll(req)).getAsJsonObject();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		result.addProperty("success", true);
		res.getWriter().print(result.toString());
		res.getWriter().close();
	}
}
