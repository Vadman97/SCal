package calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.GsonBuilder;

import usc_classes.Discussion;
import usc_classes.Lab;
import usc_classes.USCClass;
import usc_classes.USCSection;
import user.User;
import util.Util;

public class Calendar {
	private enum View {
		DAY, WEEK, MONTH
	}

	// used for maintaining information about the cache state
	public View view;
	public Date viewDate;
	
	public ArrayList<Event> events; // used for GSON object serialization
	private transient User current_user;
	public static final String ERROR_JSON = "{\"success\": false}";
	public static final String EVENT_SELECT_SQL = "SELECT * FROM Events JOIN EventRelationships ON Events.id=EventRelationships.event_id WHERE EventRelationships.user_id=?";
	public static final String CLASS_COLOR = "green", EXAM_COLOR = "red";
	public static final String[] TYPES_OF_ENROLLMENTS = {"EnrolledClasses", "EnrolledDiscussions", "EnrolledLabs"};

	public Calendar(User u) {
		current_user = u;
		events = new ArrayList<Event>();
	}

	public String getEvents(User user, Date start, View v) {
		Connection conn = null;
		try {
			conn = Util.getConn();
			PreparedStatement st = null;
			if (start != null)
				st = conn.prepareStatement(EVENT_SELECT_SQL + " AND start_time>=? AND start_time <=?");
			else 
				st = conn.prepareStatement(EVENT_SELECT_SQL);
			st.setLong(1, current_user.getId());
			
			Timestamp startTs = null;
			Timestamp endTs = null;

			if (start != null) {
				// We need to round the start day to the beginning of
				// the day to midnight and add the right offset.
	
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.setTime(start);
				c.set(java.util.Calendar.HOUR_OF_DAY, 0);
				c.set(java.util.Calendar.MINUTE, 0);
				c.set(java.util.Calendar.SECOND, 0);
				c.set(java.util.Calendar.MILLISECOND, 0);
	
				view = v;
				switch (v) {
				case WEEK:
					c.set(java.util.Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
					startTs = new Timestamp(c.getTime().getTime());
					viewDate = c.getTime();
					c.add(java.util.Calendar.DAY_OF_WEEK_IN_MONTH, 1);
					endTs = new Timestamp(c.getTime().getTime());
					break;
				case MONTH:
					c.set(java.util.Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
					c.set(java.util.Calendar.DAY_OF_WEEK_IN_MONTH, 1);
					startTs = new Timestamp(c.getTime().getTime());
					viewDate = c.getTime();
					c.add(java.util.Calendar.MONTH, 1);
					endTs = new Timestamp(c.getTime().getTime());
					break;
				default:
					startTs = new Timestamp(c.getTime().getTime());
					viewDate = c.getTime();
					c.add(java.util.Calendar.DATE, 1);
					endTs = new Timestamp(c.getTime().getTime());
					break;
				}
	
				st.setTimestamp(2, startTs);
				st.setTimestamp(3, endTs);
			}
			
			ResultSet rs = st.executeQuery();
			events.clear();
			while (rs.next()) {
				// add to events
				events.add(new Event(rs.getLong(1), rs.getString(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBoolean(8), rs.getString(11)));
			}
			
			loadEnrolledEvents(conn, startTs, endTs, user);
			
			return toString();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ERROR_JSON;
	}
	
	private void loadEnrolledEvents(Connection conn, Timestamp start_ts, Timestamp end_ts, User u) {
		try {
			for (String s : TYPES_OF_ENROLLMENTS) {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + s + " WHERE user_id=?");
				ps.setLong(1, u.getId());
				ResultSet st = ps.executeQuery();
				//TODO(Vadim) multithread this loading?
				while (st.next()) {
					long section_id = st.getLong(2);
					if (s == TYPES_OF_ENROLLMENTS[0]) {
						loadClass(conn, section_id, start_ts, end_ts);
						loadExams(conn, section_id, start_ts, end_ts);
					}
					else if (s == TYPES_OF_ENROLLMENTS[1])
						loadSection(conn, start_ts, end_ts, Discussion.get(section_id));
					else if (s == TYPES_OF_ENROLLMENTS[2])
						loadSection(conn, start_ts, end_ts, Lab.get(section_id));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadExams(Connection conn, long class_id, Timestamp start_ts, Timestamp end_ts) {
		USCClass parentClass = new USCClass(class_id);
		parentClass.load();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Exams WHERE class_id=?");
			ps.setLong(1, class_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = parentClass.getDept() + parentClass.getClass_num() + " Exam";
				String description = parentClass.getName() + " Exam";
				events.add(new Event(0, name, rs.getTimestamp(3), rs.getTimestamp(4), "TBD", description, EXAM_COLOR, true, "owner"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSection(Connection conn, Timestamp start_ts, Timestamp end_ts, USCSection cl) {
		boolean[] days = new boolean[5];
		days[0] = cl.isMon();
		days[1] = cl.isTue();
		days[2] = cl.isWed();
		days[3] = cl.isThur();
		days[4] = cl.isFri();
		
		USCClass parentClass = new USCClass(cl.getClass_id());
		parentClass.load();
		
		calculateEvents(parentClass, cl, days, start_ts, end_ts);
	}
	
	private void loadClass(Connection conn, long class_id, Timestamp start_ts, Timestamp end_ts) {
		USCClass cl = new USCClass(class_id);
		cl.load(); //TODO exam timestamp error
		boolean[] days = new boolean[5];
		days[0] = cl.isMon();
		days[1] = cl.isTue();
		days[2] = cl.isWed();
		days[3] = cl.isThur();
		days[4] = cl.isFri();
		
		calculateEvents(cl, cl, days, start_ts, end_ts);
	}
	
	public void calculateEvents(USCClass parentClass, USCSection cl, boolean[] days, Timestamp start_ts, Timestamp end_ts) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		
		if (start_ts == null) {
			int year = parentClass.getSemester() / 10;
			if (parentClass.getSemester() % 10 == 3) {
				//fall semester
				c.set(year, java.util.Calendar.AUGUST, 21);
				start_ts = new Timestamp(c.getTime().getTime());
				c.set(year, java.util.Calendar.DECEMBER, 2);
				end_ts = new Timestamp(c.getTime().getTime());
			} else if (parentClass.getSemester() % 10 == 2) {
				//summer semester
				c.set(year, java.util.Calendar.JANUARY, 9);
				start_ts = new Timestamp(c.getTime().getTime());
				c.set(year, java.util.Calendar.APRIL, 30);
				end_ts = new Timestamp(c.getTime().getTime());
			} if (parentClass.getSemester() % 10 == 1) {
				//spring semester
				c.set(year, java.util.Calendar.MAY, 17);
				start_ts = new Timestamp(c.getTime().getTime());
				c.set(year, java.util.Calendar.AUGUST, 8);
				end_ts = new Timestamp(c.getTime().getTime());
			}
		} 

		c.setTime(cl.getStart_time());
		int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
		int minute = c.get(java.util.Calendar.MINUTE);
		c.setTime(cl.getEnd_time());
		int hourEnd = c.get(java.util.Calendar.HOUR_OF_DAY);
		int minuteEnd = c.get(java.util.Calendar.MINUTE);
		c.setTime(start_ts);
		c.set(java.util.Calendar.SECOND, 0);
		c.set(java.util.Calendar.MILLISECOND, 0);
		
		c.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
		Timestamp eventStart = null, eventEnd = null;
		while (true) {
			for (boolean b: days) {
				c.add(java.util.Calendar.DAY_OF_WEEK, 1);
				if (b) {
					c.set(java.util.Calendar.HOUR_OF_DAY, hour);
					c.set(java.util.Calendar.MINUTE, minute);
					eventStart = new Timestamp(c.getTime().getTime());
					if (eventStart.after(end_ts))
						break;
					c.set(java.util.Calendar.HOUR_OF_DAY, hourEnd);
					c.set(java.util.Calendar.MINUTE, minuteEnd);
					eventEnd = new Timestamp(c.getTime().getTime());
					events.add(new Event(0, cl.getName(), eventStart, eventEnd, cl.getLocation(), cl.getDescription(), CLASS_COLOR, true, "owned"));
				}
			}
			if (eventStart.after(end_ts))
				break;
			c.add(java.util.Calendar.DAY_OF_WEEK, 2);
		}
	}
	
	public String getAll(User u) {
		return getEvents(u, null, null);
	}
	
	public String getDayEvents(User u, Date d) {
		return getEvents(u, d, View.DAY);
	}

	public String getWeekEvents(User u, Date d) {
		return getEvents(u, d, View.WEEK);
	}

	public String getMonthEvents(User u, Date d) {
		return getEvents(u, d, View.MONTH);
	}
	
	public String toString() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(this);
	}
}
