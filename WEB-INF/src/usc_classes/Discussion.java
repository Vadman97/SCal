package usc_classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Constants;
import util.Util;

public class Discussion extends USCSection {

	private long discussion_id; 
	
	public Discussion() {
		setType(Constants.discussion);
	}
	

	public long getDiscussion_id() {
		return discussion_id;
	}

	public void setDiscussion_id(long discussion_id) {
		this.discussion_id = discussion_id;
	}
	
	public static Discussion load(long class_id) {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Discussions WHERE class_id = ? LIMIT 1");
			ps.setLong(1, class_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Discussion d = new Discussion();
				d.setDiscussion_id(rs.getLong(1));
				d.setSection_id(rs.getInt(2));
				d.setClass_id(rs.getLong(3));
				d.setStart_time(rs.getTime(4));
				d.setEnd_time(rs.getTime(5));
				d.setMon(rs.getBoolean(6));
				d.setTue(rs.getBoolean(7));
				d.setWed(rs.getBoolean(8));
				d.setThur(rs.getBoolean(9));
				d.setFri(rs.getBoolean(10));
				return d;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null) con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
