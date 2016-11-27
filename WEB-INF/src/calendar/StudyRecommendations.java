package calendar;

import static jeigen.Shortcuts.zeros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jeigen.DenseMatrix;
import usc_classes.USCClass;
import user.User;
import util.Util;

public class StudyRecommendations {
	//TODO(Vadim): better heuristic for finding people
	public static final String QUERY = 
			"SELECT Friendships.student_one, Friendships.student_two, USCClasses.class_id "
			+ "FROM Friendships JOIN EnrolledClasses, USCClasses "
			+ "WHERE ((Friendships.student_one=? AND EnrolledClasses.user_id=Friendships.student_two AND EnrolledClasses.class_id=?) "
			+ "OR (Friendships.student_two=? AND EnrolledClasses.user_id=Friendships.student_one AND EnrolledClasses.class_id=?)) "
			+ "AND USCClasses.class_id = EnrolledClasses.class_id";

	public static final int START_HOUR = 9;
	public static final int END_HOUR = 22;
	public static final int TIME_QUANTS = (END_HOUR - START_HOUR) * 4;
	public static final String REC_COLOR = "#0177ff";
	
	// recommendations for users from your classes for week of focusDay
	public static Map<String, Object> getRecommendations(User currentUser, Timestamp focusDay) {
		Connection con = null;
		try {
			con = Util.getConn();
			PreparedStatement st = con.prepareStatement("SELECT class_id FROM EnrolledClasses WHERE user_id=?");
			st.setLong(1, currentUser.getId());
			ResultSet rs = st.executeQuery();
			Map<String, Object> out = new HashMap<>();
			Map<String, Vector<Event>> result = new HashMap<>();
			while (rs.next()) {
				long class_id = rs.getLong(1);
				PreparedStatement st2 = con.prepareStatement(QUERY);
				st2.setLong(1, currentUser.getId());
				st2.setLong(2, class_id);
				st2.setLong(3, currentUser.getId());
				st2.setLong(4, class_id);
				ResultSet rs2 = st2.executeQuery();
				Vector<User> users = new Vector<>();
				while (rs2.next()) {
					long studentOne = rs2.getLong(1), studentTwo = rs2.getLong(2);
					if (studentOne == currentUser.getId()) {
						users.add(User.getUser(con, studentTwo));
					} else {
						users.add(User.getUser(con, studentOne));
					}
				}
				USCClass uscclass = new USCClass(class_id);
				uscclass.load();
				result.put(uscclass.getName(), findCommonTime(users, currentUser, focusDay));
				out.put("events", result);
				out.put("users", users);
			}
			return out;
			
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
		return null;
	}
	
	private static java.util.Calendar resetCal(java.util.Calendar cal, Date focusDay, int d) {
		cal.setTime(new Date(focusDay.getTime()));
		cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
		cal.set(java.util.Calendar.MINUTE, 0);
		cal.set(java.util.Calendar.SECOND, 0);
		cal.set(java.util.Calendar.MILLISECOND, 0);
		cal.set(java.util.Calendar.DAY_OF_WEEK, d);
		return cal;
	}
	
	public static Vector<Event> findCommonTime(Vector<User> users, User currentUser, Timestamp focusDay) {
		Vector<Event> result = new Vector<>();
		java.util.Calendar cal = resetCal(java.util.Calendar.getInstance(), focusDay, java.util.Calendar.SUNDAY);
		//Week at a time around focusDay
		for (int d = java.util.Calendar.SUNDAY; d <= java.util.Calendar.SUNDAY + 6; d++) {
			DenseMatrix matrix = zeros(users.size(), TIME_QUANTS);
			for (int i = 0; i < users.size(); i++) {
				Calendar calendar = new Calendar(users.get(i));
				cal = resetCal(cal, focusDay, d);
				calendar.getDayEvents(currentUser, cal.getTime());
				ArrayList<Event> events = calendar.events;
				for (Event e: events) {
					cal.setTime(e.getStartTimestamp());
					int start = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
					cal.setTime(e.getEndTimestamp());
					int end = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
					// fill row region from start to end with 1s
					for (int k = start; k <= end; k++) {
						if (k >= TIME_QUANTS)
							break;
						matrix.set(i, k, 1.0);
					}
				}
			}
			cal = resetCal(cal, focusDay, d);
			matrix = matrix.sumOverRows();
			/*
			 * These operations find blocks of no-conflict
			 * Ex: 
			 * 			User 1: 0.00000  0.00000  1.00000  1.00000  0.00000
			 * 			User 2: 1.00000  1.00000  1.00000  1.00000  0.00000
			 * 			User 3: 0.00000  0.00000  0.00000  0.00000  0.00000
			 * 			Result: 1.00000  1.00000  2.00000  2.00000  0.00000
			 * 	
			 * 			The region of no conflict is the last column in this example
			 * 			In these calculations, each column is assumed 15 minutes, 
			 * 			ranging from START_HOUR to END_HOUR
			 */
			String friends = "";
			for (User u: users)
				friends += u.getUsername() + " ";
			
			int start = 0;
			boolean val = false;
			//find continuous blocks of no-conflicts and create events from those
			for (int i = 0; i < matrix.cols; i++) {
				val = matrix.get(0, i) == 0.0;
				if (!val) {
					if ((i - 1) > start) {
						eventCalculation(cal, i, start, result, friends);
					}
					start = i;
				}
			}
			if (!val) {
				eventCalculation(cal, matrix.cols, start, result, friends);
			}
		}
		
		return result;
	}
	
	private static void eventCalculation(java.util.Calendar cal, int idx, int start, Vector<Event> result, String frs) {
		int eventEnd = idx - 1;
		cal.set(java.util.Calendar.HOUR_OF_DAY, start / 4 + START_HOUR);
		cal.set(java.util.Calendar.MINUTE, (start * 15 % 60));
		Timestamp startTs = new Timestamp(cal.getTime().getTime());
		cal.set(java.util.Calendar.HOUR_OF_DAY, eventEnd / 4 + START_HOUR);
		cal.set(java.util.Calendar.MINUTE, (eventEnd * 15 % 60));
		Timestamp endTs = new Timestamp(cal.getTime().getTime());
		Event e = new Event(0, "Study Recommendation with " + frs, startTs, endTs, "TBD", "Study with your friends " + frs, REC_COLOR, true, "owned");
		result.add(e);
	}
	
//	public static Event findCommonTime(Vector<User> users, User currentUser, HttpServletRequest req) {  
//		DenseMatrix matrix = zeros(users.size(), TIME_QUANTS);
//		for (int i = 0; i < users.size(); i++) {
//			Calendar c = new Calendar(users.get(i));
//			c.getAll(req);
//			ArrayList<Event> events = c.events;
//			for (Event e: events) {
//				//TODO have to go day by day, week at a time?
//				java.util.Calendar cal = java.util.Calendar.getInstance();
//				cal.setTime(e.getStartTimestamp());
//				int start = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
//				cal.setTime(e.getEndTimestamp());
//				int end = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
//				// fill row region from start to end with 1s
//				for (int k = start; k <= end; k++)
//					matrix.set(i, k, 1.0);
//			}
//		}
//
//		matrix = matrix.sumOverRows();
//		
//		return null;
//	}
	
}
