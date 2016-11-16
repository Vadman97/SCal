package calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import user.User;
import util.Util;

public class Calendar {
	private enum View {
		DAY, WEEK, MONTH
	}

	private View view;
	private Date viewDate;
	private ArrayList<Event> events; //used for GSON object serialization
	private transient User current_user;
	public static final String ERROR_JSON = "{success: false}";
	public static final String EVENT_SELECT_SQL = "SELECT * FROM Events JOIN EventRelationships ON Events.id=EventRelationships.event_id WHERE EventRelationships.user_id=?";

	public Calendar(User u) {
		current_user = u;
		events = new ArrayList<Event>();
	}

	public String getEvents(Date start, View v) {
		Connection conn = null;
		try {
			conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement(EVENT_SELECT_SQL + " AND start_time>=? AND start_time <=?");
			st.setLong(1, current_user.getId());
			
			//TODO(Vadim): We need to round the start day to the beginning of the day to midnight
			//				and add the right offset. Months have different logic? (How many days in the month)
			java.util.Calendar c = java.util.Calendar.getInstance();
			c.setTime(start);
			st.setTimestamp(2, new Timestamp(c.getTime().getTime()));
			
			int dayOffset = 0;
			switch (v) {
				case WEEK: dayOffset = 7; break;
				case MONTH: /*TODO(Vadim)*/ break;
				default: dayOffset = 1; break;
			}
			c.add(java.util.Calendar.DATE, dayOffset);
			st.setTimestamp(3, new Timestamp(c.getTime().getTime()));
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				//add to events
			}
			//cache the json string as the current string
			//maybe based on view / viewdate?
			//return json string
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

	public String getDayEvents(Date d) {
		return getEvents(d, View.DAY);
	}

	public String getWeekEvents(Date d) {
		return getEvents(d, View.WEEK);
	}

	public String getMonthEvents(Date d) {
		return getEvents(d, View.MONTH);
	}
}
