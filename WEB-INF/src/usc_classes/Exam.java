package usc_classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import util.Util;

public class Exam {

	private long exam_id, class_id;
	Timestamp start, end;
	
	protected Exam() {}
	
	public Exam(long exam_id, long class_id, Timestamp start, Timestamp end) {
		this.exam_id = exam_id;
		this.class_id = class_id;
		this.start = start;
		this.end = end;
	}
	public long getExam_id() {
		return exam_id;
	}
	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}
	public long getClass_id() {
		return class_id;
	}
	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	} 
	
	public static Vector<Exam> load(long class_id) {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Exams WHERE class_id = ?");
			ps.setLong(1, class_id);
			ResultSet rs = ps.executeQuery();
			Vector<Exam> exams = new Vector<>();
			while (rs.next()) {
				Exam ex = new Exam();
				ex.setExam_id(rs.getLong(1));
				ex.setClass_id(rs.getLong(2));
				ex.setStart(rs.getTimestamp(3));
				ex.setEnd(rs.getTimestamp(4));
				exams.add(ex);
			}
			if (exams.size() > 0)
				return exams;
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
