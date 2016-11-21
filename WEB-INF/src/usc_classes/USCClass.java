package usc_classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Vector;

import main.Constants;
import util.Util;

public class USCClass extends USCSection {

	long class_id;
	private int class_num; 
	private Vector<Exam> exams = new Vector<Exam>(); 
	private Vector<Discussion> discussions = new Vector<>();
	private Vector<Lab> labs = new Vector<>();

	private String professor;

	private int semester;
	
	public USCClass(long class_id) {
		setClass_id(class_id);
	}
	
	public USCClass(int section_id) {
		setSection_id(section_id);
	}

	protected USCClass(int section_id, int class_id, String dept, String location, String name, 
			Time start_time, Time end_time, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			int semester, String professor, String description, int class_num) {
		super(section_id, class_id, dept, location, name, start_time, end_time, mon, tue, wed, thur, fri, description);
		
		setClass_id(class_id);
		setClass_num(class_num);
		setSemester(semester);
		setProfessor(professor);
		setType(Constants.classSection);
	}

	public long getClass_id() {
		return class_id;
	}
	public int getClass_num() {
		return class_num;
	}
	
	public Vector<Exam> getExams() {
		return exams;
	}

	public String getProfessor() {
		return professor;
	}

	public int getSemester() {
		return semester;
	}

	public Vector<Discussion> getDiscussions() {
		return discussions;
	}

	public Vector<Lab> getLabs() {
		return labs;
	}
	
	public void load() {
		if(getClass_id() != 0) {
			Connection con = null;
			try {
				con = Util.getConn();
				PreparedStatement ps;
				if (getClass_id() == 0) {
					ps = con.prepareStatement("SELECT * FROM USCClasses WHERE section_id=?");
					ps.setInt(1, getSection_id());
				} else {
					ps = con.prepareStatement("SELECT * FROM USCClasses WHERE class_id=?");
					ps.setLong(1, getClass_id());
				}
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					setInternalData(rs);
					exams = Exam.load(getClass_id());
				} else {
					setClass_id(0);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if(con != null) con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					
				}
			}
		}
	}

	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}
	
	public void setClass_num(int class_num) {
		this.class_num = class_num;
	}

	private void setInternalData(ResultSet rs) {
		try {
			setSection_id(rs.getInt(2));
			setDept(rs.getString(3));
			setClass_num(rs.getInt(4));
			setSemester(rs.getInt(5));
			setName(rs.getString(6));
			setProfessor(rs.getString(7));
			setLocation(rs.getString(8));
			setStart_time(rs.getTime(9));
			setEnd_time(rs.getTime(10));
			setMon(rs.getBoolean(11));
			setTue(rs.getBoolean(12));
			setWed(rs.getBoolean(13));
			setThur(rs.getBoolean(14));
			setFri(rs.getBoolean(15));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	private void setPSData(PreparedStatement ps) {
		try {
			ps.setInt(1, getSection_id());
			ps.setString(2, getDept());
			ps.setInt(3, getClass_num());
			ps.setInt(4, getSemester());
			ps.setString(5, getName());
			ps.setString(6, getProfessor());
			ps.setString(7, getLocation());
			ps.setTime(8, getStart_time());
			ps.setTime(9, getEnd_time());
			ps.setBoolean(10, isMon());
			ps.setBoolean(11, isTue());
			ps.setBoolean(12, isWed());
			ps.setBoolean(13, isThur());
			ps.setBoolean(14, isFri());
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setSemester(int semester) {
		this.semester = semester;
	}

	//NOTE this method is not really necessary if we manually load in these classes into the database 
	public void write() {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement ps = null;
			ResultSet rs = null; 
			boolean insert = false; 
			
			if(class_id != 0) {
				//get event
				PreparedStatement pullClass = con.prepareStatement("SELECT class_id from USCClass WHERE "
						+ "class_id=?");
				pullClass.setLong(1, class_id);
				rs = pullClass.executeQuery();
			}
			
			if(class_id != 0 && rs.next()) {
				//event exists, update changed parameters
				ps = con.prepareStatement("UPDATE USCClass SET section_id=?, dept=?, class_num=?, semester=?, "
						+ "name=?, professor=?, location=?, start_time=?, end_time=?, monday=?, "
						+ "tuesday=?, wednesday=?, thursday=?, friday=? WHERE class_id=?");
				ps.setLong(15, class_id);
			} else {
				insert = true; 
				ps = con.prepareStatement("INSERT INTO USCClass (section_id, dept, class_num, semester, "
						+ "name, professor, location, start_time, end_time, monday, tuesday, wednesday, "
						+ "thursday, friday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
						Statement.RETURN_GENERATED_KEYS);
			}
			setPSData(ps);
			ps.executeUpdate();
			con.commit();
			if(insert) {
				ResultSet keys = ps.getGeneratedKeys();
				if(keys.next()) {
					setClass_id(keys.getLong(1));
				}
			}
			
			//TODO(Vadim) write children classes
//			if (discussion != null)
//				discussion.write();
//			
//			if (lab != null)
//				lab.write();
//			
//			for (Exam ex: exams) {
//				if (ex != null)
//					ex.write();
//			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null) con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
