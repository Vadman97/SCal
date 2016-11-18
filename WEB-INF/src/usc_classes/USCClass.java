package usc_classes;

import java.sql.*;

import main.Constants;
import user.User;
import util.Util;

public class USCClass extends USCSection {

	private int class_num;
	long class_id; 
	private int semester; 
	private String professor; 
	private Discussion discussion = null;
	private Lab lab = null;
	private Exam exam  =null;
	
	public USCClass(int section_id, int class_id, String dept, String location, String name, 
			Timestamp start_time, Timestamp end_time, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			int semester, String professor, String description, int class_num) {
		super(section_id, dept, location, name, start_time, end_time, mon, tue, wed, thur, fri, description);
		
		setClass_id(class_id);
		setClass_num(class_num);
		setSemester(semester);
		setProfessor(professor);
		setType(Constants.classSection);
	}

	public int getClass_num() {
		return class_num;
	}

	public void setClass_num(int class_num) {
		this.class_num = class_num;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	public long getClass_id() {
		return class_id;
	}

	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}
	
	
	/*=======================================================================================
	 * DATABASE
	 *======================================================================================= */
	
	private void write(User current_user) {
		Connection con = null;
		try {
			con = Util.getConn();
			con.setAutoCommit(false);
			
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
			//is con.commit() and con.setAutoCommit(true) necessary here?
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
	
	private void load() {
		if(getClass_id() != 0) {
			Connection con = null;
			try {
				con = Util.getConn();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM USCClasses WHERE class_id=?");
				ps.setLong(1, getClass_id());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					setInternalData(rs);
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
	
	private void setPSData(PreparedStatement ps) {
		try {
			ps.setInt(1, getSection_id());
			ps.setString(2, getDept());
			ps.setInt(3, getClass_num());
			ps.setInt(4, getSemester());
			ps.setString(5, getName());
			ps.setString(6, getProfessor());
			ps.setString(7, getLocation());
			ps.setTimestamp(8, getStart_time());
			ps.setTimestamp(9, getEnd_time());
			ps.setBoolean(10, isMon());
			ps.setBoolean(11, isTue());
			ps.setBoolean(12, isWed());
			ps.setBoolean(13, isThur());
			ps.setBoolean(14, isFri());
		} catch(SQLException e) {
			e.printStackTrace();
		}
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
			setStart_time(rs.getTimestamp(9));
			setEnd_time(rs.getTimestamp(10));
			setMon(rs.getBoolean(11));
			setTue(rs.getBoolean(12));
			setWed(rs.getBoolean(13));
			setThur(rs.getBoolean(14));
			setFri(rs.getBoolean(15));
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
}
