package usc_classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Vector;

import main.Constants;
import util.Util;

public class Lab extends USCSection {
	
	private long lab_id;
	
	protected Lab() {};

	public Lab(int section_id, int class_id, String dept, String location, String name, Time start, Time end, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			String description, long lab_id) {
		super(section_id, class_id, dept, location, name, start, end, mon, tue, wed, thur, fri, description);
		
		this.lab_id = lab_id; 
		setType(Constants.lab);
		
	}

	public long getLab_id() {
		return lab_id;
	}

	public void setLab_id(long lab_id) {
		this.lab_id = lab_id;
	}

	public static Vector<Lab> load(long class_id) {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Labs WHERE class_id = ? LIMIT 1");
			ps.setLong(1, class_id);
			ResultSet rs = ps.executeQuery();
			Vector<Lab> labs = new Vector<>();
			if (rs.next()) {
				Lab l = new Lab();
				l.setLab_id(rs.getLong(1));
				l.setSection_id(rs.getInt(2));
				l.setClass_id(rs.getLong(3));
				l.setStart_time(rs.getTime(4));
				l.setEnd_time(rs.getTime(5));
				l.setMon(rs.getBoolean(6));
				l.setTue(rs.getBoolean(7));
				l.setWed(rs.getBoolean(8));
				l.setThur(rs.getBoolean(9));
				l.setFri(rs.getBoolean(10));
				labs.add(l);
			}
			if (labs.size() > 0)
				return labs;
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
