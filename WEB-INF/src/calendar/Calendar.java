package calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.GsonBuilder;

import user.User;
import util.Util;

public class Calendar {
	private enum View {
		DAY, WEEK, MONTH
	}

	// used for maintaining information about the cache state
	public View view;
	public Date viewDate;
	
	private ArrayList<Event> events; // used for GSON object serialization
	private transient User current_user;
	public static final String ERROR_JSON = "{success: false}";
	public static final String EVENT_SELECT_SQL = "SELECT * FROM Events JOIN EventRelationships ON Events.id=EventRelationships.event_id WHERE EventRelationships.user_id=?";

	public Calendar(User u) {
		current_user = u;
		events = new ArrayList<Event>();
	}

	public String getEvents(HttpServletRequest req, Date start, View v) {
		Connection conn = null;
		try {
			conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement(EVENT_SELECT_SQL + " AND start_time>=? AND start_time <=?");
			st.setLong(1, current_user.getId());

			// We need to round the start day to the beginning of
			// the day to midnight and add the right offset.
			Timestamp startTs = null;
			Timestamp endTs = null;

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

			// cache the current calendar based on view / viewdate?
			// cached in backend session
//			if (req != null && req.getSession().getAttribute("calendar") != null) {
//				if (req.getSession().getAttribute("calendar") instanceof Calendar) {
//					Calendar cal = (Calendar) req.getSession().getAttribute("calendar");
//					if (cal.view.equals(view) && cal.viewDate.equals(viewDate))
//						return cal.toString();
//				}
//			}
			
			st.setTimestamp(2, startTs);
			st.setTimestamp(3, endTs);
			ResultSet rs = st.executeQuery();
			events.clear();
			while (rs.next()) {
				// add to events
				events.add(new Event(rs.getLong(1), rs.getString(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getBoolean(8), rs.getString(11)));
			}
			
//			if (req != null)
//				 req.getSession().setAttribute("calendar", this);
			
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

	public String getDayEvents(HttpServletRequest req, Date d) {
		return getEvents(req, d, View.DAY);
	}

	public String getWeekEvents(HttpServletRequest req, Date d) {
		return getEvents(req, d, View.WEEK);
	}

	public String getMonthEvents(HttpServletRequest req, Date d) {
		return getEvents(req, d, View.MONTH);
	}
	
	public String toString() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(this);
	}
}
