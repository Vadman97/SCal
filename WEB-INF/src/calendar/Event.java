package calendar;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gson.Gson;

import user.User;
import util.Util;

//import java.util.Date;

public class Event {
	public final class RelationshipTypes {
		public final static String OWNED = "owned";
		public final static String SHARED = "shared";
	}
	 
	private long id;
	private String name;
	private Timestamp startTimestamp, endTimestamp; 
	private String location, description; 
	private Color color; 
	private boolean notify;
	
	private transient Vector<User> shared;
	
	// used from Calendar when loading all events from db
	public Event(long id, String name, Timestamp start, Timestamp end, String location, String description, Color color, boolean notify) {
		shared = new Vector<User>();
		setId(id);
		setName(name);
		setStartTimestamp(start);
		setEndTimestamp(end);
		setLocation(location);
		setDescription(description);
		setColor(color);
		setNotify(notify);
		loadSharedWith();
	}

	public static Event parse(String json) {
		Event e = new Gson().fromJson(json, Event.class);
		e.loadSharedWith();
		return e;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public void write(User current_user) {
		Connection conn = null;
		try {
			conn = Util.getConn();
			conn.setAutoCommit(false);
			
			PreparedStatement st = conn.prepareStatement("SELECT id FROM Events WHERE id=?");
			st.setLong(0, id);
			ResultSet rs = st.executeQuery();
			PreparedStatement st2 = null;
			if (rs.next()) {
				// event exists, update
				st2 = conn.prepareStatement("UPDATE Events SET name=?, start_time=?, end_time=?, location=?, color=?, notify=? WHERE id=?");
				st2.setLong(7, id);
			} else {
				// event does not exist
				st2 = conn.prepareStatement("INSERT INTO Events (name, start_time, end_time, location, color, notify) VALUES (?, ?, ?, ?, ?, ?)");
			}
			st2.setString(1, name);
			st2.setTimestamp(2, startTimestamp);
			st2.setTimestamp(3, endTimestamp);
			st2.setString(4, location);
			st2.setString(5, getColorName());
			st2.setBoolean(6, notify);
			st2.executeUpdate();
			
			// write own relationship if noone owns this event
			st2 = conn.prepareStatement("SELECT 0 FROM EventRelationships WHERE event_id=? and relationship_type=?");
			st2.setLong(1, id);
			st2.setString(2, RelationshipTypes.OWNED);
			if (!st2.executeQuery().next()) 
				createRelationship(conn, current_user, RelationshipTypes.OWNED);

			// write shared relationships
			for (User u: shared) {
				createRelationship(conn, u, RelationshipTypes.SHARED);
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void createRelationship(Connection conn, User user, String type) throws SQLException {
		try {
			PreparedStatement st = conn.prepareStatement("INSERT INTO EventRelationships VALUES (?, ?, ?)");
			st.setLong(1, user.getId());
			st.setLong(2, id);
			st.setString(3, type);
		} catch (SQLException e) {
			// INSERT IF NOT EXISTS by virtue of unique constraint 
			if (e.getErrorCode() != 1062)
				throw e;
		}
	}
	
	private String getColorName() {
		// 'red','orange','yellow','green','blue','cyan','magenta'
		if (color.equals(Color.RED)) 
			return "red";
		else if (color.equals(Color.ORANGE))
			return "orange";
		else if (color.equals(Color.YELLOW))
			return "yellow";
		else if (color.equals(Color.GREEN))
			return "green";
		else if (color.equals(Color.CYAN))
			return "cyan";
		else if (color.equals(Color.MAGENTA))
			return "magenta";
		else
			return "blue";
	}
	
	private void loadSharedWith() {
		try {
			Connection conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement("SELECT * FROM EventRelationships WHERE event_id=?");
			st.setLong(0, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				shared.add(User.getUser(conn, rs.getLong(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStartTimestamp() {
		return startTimestamp;
	}
	
	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}

	public void setStartTimestamp(Timestamp ts) {
		this.startTimestamp = ts;
	}
	
	public void setEndTimestamp(Timestamp ts) {
		this.endTimestamp = ts;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	
	
}
