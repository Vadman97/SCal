package calendar;

import static jeigen.Shortcuts.zeros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jeigen.DenseMatrix;
import user.User;
import util.Util;

public class StudyRecommendations {
	public static final String QUERY = 
			"SELECT Friendships.student_one, Friendships.student_two, USCClasses.class_id "
			+ "FROM Friendships JOIN EnrolledClasses, USCClasses "
			+ "WHERE (Friendships.student_one=? AND EnrolledClasses.user_id=Friendships.student_two AND EnrolledClasses.class_id=?) "
			+ "OR (Friendships.student_two=? AND EnrolledClasses.user_id=Friendships.student_one AND EnrolledClasses.class_id=?)) "
			+ "AND USCClasses.class_id = EnrolledClasses.class_id";

	public static final int START_HOUR = 9;
	public static final int END_HOUR = 22;
	public static final int TIME_QUANTS = (END_HOUR - START_HOUR) * 4;
	public static final String REC_COLOR = "orange";
	
	public static Event getRecommendations(User currentUser) {
		try {
			Connection con = Util.getConn();
			PreparedStatement st = con.prepareStatement("SELECT class_id FROM EnrolledClasses WHERE user_id=?");
			st.setLong(1, currentUser.getId());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				long class_id = rs.getLong(1);
				PreparedStatement st2 = con.prepareStatement(QUERY);
				st2.setLong(1, currentUser.getId());
				st2.setLong(2, class_id);
				st2.setLong(3, currentUser.getId());
				st2.setLong(4, class_id);
				ResultSet rs2 = st2.executeQuery();
				while (rs2.next()) {
					long studentOne = rs2.getLong(1), studentTwo = rs2.getLong(2);
					if (studentOne == currentUser.getId()) {
						
					} else {
						
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public static Vector<Event> findCommonTime(Vector<User> users, User currentUser, Timestamp focusDay, HttpServletRequest req) {
		Vector<Event> result = new Vector<>();
		java.util.Calendar cal = resetCal(java.util.Calendar.getInstance(), focusDay, java.util.Calendar.SUNDAY);
		//Week at a time around focusDay
		for (int d = java.util.Calendar.SUNDAY; d <= java.util.Calendar.SUNDAY + 6; d++) {
			DenseMatrix matrix = zeros(users.size(), TIME_QUANTS);
			for (int i = 0; i < users.size(); i++) {
				Calendar calendar = new Calendar(users.get(i));
				cal = resetCal(cal, focusDay, d);
				calendar.getDayEvents(req, cal.getTime());
				ArrayList<Event> events = calendar.events;
				for (Event e: events) {
					cal.setTime(e.getStartTimestamp());
					int start = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
					cal.setTime(e.getEndTimestamp());
					int end = (cal.get(java.util.Calendar.HOUR_OF_DAY) - START_HOUR) * 4 + cal.get(java.util.Calendar.MINUTE / 15);
					// fill row region from start to end with 1s
					for (int k = start; k <= end; k++)
						matrix.set(i, k, 1.0);
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
			int start = 0;
			boolean val = false;
			//find continuous blocks of no-conflicts and create events from those
			for (int i = 0; i < matrix.cols; i++) {
				val = matrix.get(0, i) == 0.0;
				if (!val) {
					if ((i - 1) > start) {
						eventCalculation(cal, i, start, result);
					}
					start = i;
				}
			}
			if (!val) {
				eventCalculation(cal, matrix.cols, start, result);
			}
		}
		
		return result;
	}
	
	private static void eventCalculation(java.util.Calendar cal, int idx, int start, Vector<Event> result) {
		int eventEnd = idx - 1;
		cal.set(java.util.Calendar.HOUR_OF_DAY, start / 4 + START_HOUR);
		cal.set(java.util.Calendar.MINUTE, (start * 15 % 60));
		Timestamp startTs = new Timestamp(cal.getTime().getTime());
		cal.set(java.util.Calendar.HOUR_OF_DAY, eventEnd / 4 + START_HOUR);
		cal.set(java.util.Calendar.MINUTE, (eventEnd * 15 % 60));
		Timestamp endTs = new Timestamp(cal.getTime().getTime());
		Event e = new Event(0, "Study Recommendation", startTs, endTs, "TBD", "TBD", REC_COLOR, true, "owned");
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
