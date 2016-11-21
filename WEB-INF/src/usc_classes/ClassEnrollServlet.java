package usc_classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}

		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("section_id")) {
			Util.close(res, false);
			return;
		}
		
		int section_id = obj.get("section_id").getAsInt();

		Connection con = null;
		try {
			con = Util.getConn();
			USCClass cl = new USCClass(section_id);
			cl.load();
			if (cl.getClass_id() != 0) {
				enroll(con, "EnrolledClasses", "class_id", u.getId(), section_id);
			} else {
				int did = isType(con, section_id, "SELECT COUNT(*), discussion_id FROM Discussions WHERE section_id=?");
				if (did != 0) {
					enroll(con, "EnrolledDiscussions", "discussion_id", u.getId(), did);
				} else {
					int lid = isType(con, section_id, "SELECT COUNT(*), lab_id FROM Labs WHERE section_id=?");
					if (lid != 0)
						enroll(con, "EnrolledLabs", "lab_id", u.getId(), lid);
				}
			}
			Util.close(res, true);
		} catch (SQLException e) {
			if (e.getErrorCode() != 1062)
				e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Util.close(res, false);
	}
	
	private void enroll(Connection con, String table, String id_name, long uid, long sid) throws SQLException {
		PreparedStatement st = con.prepareStatement("INSERT INTO " + table + " (user_id, " + id_name + ") VALUES (?, ?)");
		st.setLong(1, uid);
		st.setLong(2, sid);
		st.executeUpdate();
	}
	
	private void unenroll(Connection con, String table, String id_name, long uid, long sid) throws SQLException {
		PreparedStatement st = con.prepareStatement("DELETE FROM " + table + " WHERE user_id=? AND " + id_name + "=?");
		st.setLong(1, uid);
		st.setLong(2, sid);
		st.executeUpdate();
	}
	
	private int isType(Connection con, int section_id, String query) throws SQLException {
		PreparedStatement st = con.prepareStatement(query);
		st.setLong(1, section_id);
		ResultSet rs = st.executeQuery();
		rs.next();
		return rs.getInt(1) > 0 ? rs.getInt(2) : 0;
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// unenroll
		User u = Util.getSessionUser(req);
		if (u == null || !u.isLoggedIn()) {
			Util.close(res, false);
			return;
		}
		
		JsonObject obj = Util.parseJson(req, res);
		if (!obj.has("section_id")) {
			Util.close(res, false);
			return;
		}
		
		int section_id = obj.get("section_id").getAsInt();

		Connection con = null;
		try {
			con = Util.getConn();
			USCClass cl = new USCClass(section_id);
			cl.load();
			if (cl.getClass_id() != 0) {
				unenroll(con, "EnrolledClasses", "class_id", u.getId(), section_id);
			} else {
				int did = isType(con, section_id, "SELECT COUNT(*), discussion_id FROM Discussions WHERE section_id=?");
				if (did != 0) {
					unenroll(con, "EnrolledDiscussions", "discussion_id", u.getId(), did);
				} else {
					int lid = isType(con, section_id, "SELECT COUNT(*), lab_id FROM Labs WHERE section_id=?");
					if (lid != 0)
						unenroll(con, "EnrolledLabs", "lab_id", u.getId(), lid);
				}
			}
			Util.close(res, true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Util.close(res, false);
	}
}
