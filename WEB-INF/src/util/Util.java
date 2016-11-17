package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.User;

public class Util {

	public static Connection getConn() throws SQLException {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/db");
			return ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getRequestData(HttpServletRequest req) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
			return jb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static User getSessionUser(HttpServletRequest req) {
		Object obj = req.getSession().getAttribute("user");
		if (obj != null && obj instanceof User)
			return (User) obj;
		return null;
	}
	
	public static void close(HttpServletResponse res, boolean valid) {
		try {
			res.getWriter().println("{success: " + valid + "}");
			res.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JsonObject parseJson(HttpServletRequest req, HttpServletResponse res) {
		String requestData = Util.getRequestData(req);
		return new JsonParser().parse(requestData).getAsJsonObject();
	}
}
