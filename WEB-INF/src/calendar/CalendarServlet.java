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
		if (u == null) {
			Util.close(res, false);
			return;
		}
		
		if (req.getParameter("day") == null || req.getParameter("view") == null) {
			Util.close(res, false);
			return;
		}
		
		String dayStr = req.getParameter("day").toLowerCase();
		String viewStr = req.getParameter("view");
		
		Calendar c = new Calendar(u);

		JsonObject result = new JsonObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date day = sdf.parse(dayStr);
			if (viewStr.equals("week"))
				result = new JsonParser().parse(c.getWeekEvents(req, day)).getAsJsonObject();
			else if (viewStr.equals("month"))
				result = new JsonParser().parse(c.getMonthEvents(req, day)).getAsJsonObject();
			else 
				result = new JsonParser().parse(c.getDayEvents(req, day)).getAsJsonObject();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		result.addProperty("success", true);
		res.getWriter().print(result.toString());
		res.getWriter().close();
	}
}
