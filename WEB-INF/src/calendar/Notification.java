package calendar;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.google.gson.GsonBuilder;
import com.mysql.jdbc.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import user.User;
import util.Util;

public class Notification {
	public class NotificationType {
		public static final String EMAIL = "email";
		public static final String ONLINE = "online";
	}
	
	public static final String LOAD_ALL_SQL = "SELECT * FROM Notifications WHERE user_id=?";
	public static final String DEFAULT_TYPE = NotificationType.ONLINE;
	
	private long id;
	private long user_id;
	private long event_id;
	private String notification_type;
	private boolean completed;
	
	// for creating a notification to write
	public Notification(long user_id, long event_id, String notification_type) {
		this.user_id = user_id;
		this.event_id = event_id;
		this.notification_type = notification_type;
	}
	
	// for loading notification to write
	protected Notification(long id, long user_id, long event_id, String notification_type, boolean completed) {
		this(user_id, event_id, notification_type);
		this.id = id;
		this.completed = completed;
	}
	
	public long getId() {
		return id;
	}

	public long getUser_id() {
		return user_id;
	}

	public long getEvent_id() {
		return event_id;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public boolean isCompleted() {
		return completed;
	}
	
	public String toString() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(this);
	}

	public boolean write() {
		try { 
			Connection con = Util.getConn();
			PreparedStatement st = con.prepareStatement("INSERT INTO Notification (user_id, event_id, notification_type, completed) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setLong(1, user_id);
			st.setLong(2, event_id);
			st.setString(3, notification_type);
			st.setBoolean(4, completed);
			st.executeUpdate();
			
			ResultSet keys = st.getGeneratedKeys();
			if (keys.next()) {
				id = keys.getLong(1);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Vector<Notification> loadAll(User u, boolean completed_only) {
		if (u != null) {
			Vector<Notification> result = new Vector<>();
			
			try { 
				Connection con = Util.getConn();
				String str = completed_only ? LOAD_ALL_SQL + " AND completed=?" : LOAD_ALL_SQL;
				PreparedStatement st = con.prepareStatement(str);
				st.setLong(1, u.getId());
				if (completed_only) st.setBoolean(2, completed_only);
				
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					result.add(new Notification(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getString(4), rs.getBoolean(5)));
				}
				return result;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
