package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Util;

public class Test extends HttpServlet {
	private static final long serialVersionUID = 5729439952621174381L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("Hello, world!");

		Connection conn = null;
		try {
			// Allocate and use a connection from the pool
			conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement("SELECT * FROM Users");
			ResultSet r = st.executeQuery();
			while (r.next()) {
				out.println(r.getInt(1) + " " + r.getString(2) + " " + r.getString(3));
			}
			st = conn.prepareStatement("SELECT COUNT(*) FROM Users");
			r = st.executeQuery();
			if (r.next())
				out.println("There are " + r.getInt(1) + " users!");
		} catch (SQLException e) {
			out.println(e.toString());
		} finally {
			try {
				if (conn != null) {
					if (!conn.isClosed())
						conn.close();
				}
			} catch (SQLException e) {
				out.println(e.getMessage());
			}
		}
		
//		RequestDispatcher o = req.getRequestDispatcher("/test.jsp");
//		o.forward(req, res);

		out.flush();
		out.close();
	}
}
