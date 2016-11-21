package calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import user.User;
import util.Util;

public class ICSServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439913621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//load calendar		
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}
		
		Part filePart = req.getPart("ics"); // Retrieves <input type="file" name="ics">
	    InputStream fileContent = filePart.getInputStream();
	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(fileContent, "UTF-8"));
	    String result = "";
	    while (br.ready()) {
	    	result += br.readLine() + '\n';
	    }
	    // DO SOMETHING WITH RESULT STRING
	}
}
