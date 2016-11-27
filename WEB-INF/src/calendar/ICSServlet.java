package calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import user.User;
import util.Util;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 20,
					maxFileSize = 1024 * 1024 * 1000,
					maxRequestSize = 1024 * 1024 * 5000) 
public class ICSServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// load calendar
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}

		Part filePart = null;
		for (Part part : req.getParts()) {
			filePart = part;
			break;
		}

		InputStream fileContent = filePart.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"));

		Set<Event> events = icsHandler.importICS(br);
		for (Event e : events) {
			e.setId(0);
			e.write(u);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO(Vadim) SET HEADERS SO THIS IS A FILE

		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}
		Calendar c = new Calendar(u);
		c.getAll(u);
		String ics = icsHandler.exportICS(c.events);

		res.setHeader("Content-Type", "text/calendar");
		res.setHeader("Content-Length", "" + (ics.length() * 2)); // 2 bytes per
																	// char
		res.setHeader("Content-Transfer-Encoding", "binary");
		res.setHeader("Content-Disposition", "attachment; filename=\"cal.ics\"");
		res.getWriter().print(ics);
		res.getWriter().close();
	}
}
