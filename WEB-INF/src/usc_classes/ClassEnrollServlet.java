package usc_classes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import user.User;
import util.Util;

public class ClassEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 5229439952621174381L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// enroll
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}

		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("class_id")) {
			Util.close(res, false);
			return;
		}

//		try {
//			//TODO enroll in a class
//			Util.close(res, true);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// unenroll
		User u = Util.getSessionUser(req);
		if (u == null) {
			Util.close(res, false);
			return;
		}
		
		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("class_id")) {
			Util.close(res, false);
			return;
		}

//		try {
//			//TODO unenroll in a class
//			Util.close(res, true);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
	}
}
