package calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Statement;

import user.User;
import util.Util;

public class Event {
	public final class RelationshipType {
		public final static String OWNED = "owned";
		public final static String SHARED = "shared";
	}
	
//	private static final String[] expectedParams = {
//		"name", "start_time", "end_time", "location", "description", "color", "notify"
//	};
	
	private long id;
	private String name;
	private Timestamp start_time, end_time; 
	private String location, description; 
	private String color; 
	private String relationship;
	private boolean notify;
	
	private transient Vector<User> shared = new Vector<User>();
	
	// used from Calendar when loading all events from db
	public Event(long id, String name, Timestamp start, Timestamp end, String location, String description, String color, boolean notify, String relationship) {
		setId(id);
		setName(name);
		setStartTimestamp(start);
		setEndTimestamp(end);
		setLocation(location);
		setDescription(description);
		setColor(color);
		setNotify(notify);
		setRelationship(relationship);
		loadSharedWith();
	}
	
	public Event(long id) {
		setId(id);
		load();
	}

	public static Event parse(String json) {
		Event e = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(json, Event.class);
		if (e != null) {
			e.shared = new Vector<User>(); 
			e.loadSharedWith();
		}
		return e;
	}
	
	public static Event update(String json) {
		JsonObject jobj = new JsonParser().parse(json).getAsJsonObject();
		if (!jobj.has("id"))
			return null;
		Event e = new Event(jobj.get("id").getAsLong());
		if (e.getId() == 0)
			return null;
		Event updates = Event.parse(json);
		if (updates == null) 
			return null;
		
		if (updates.getColor() != null)
			e.setColor(updates.getColor());
		if (updates.getDescription() != null)
			e.setDescription(updates.getDescription());
		if (updates.getEndTimestamp() != null)
			e.setEndTimestamp(updates.getEndTimestamp());
		if (updates.getLocation() != null)
			e.setLocation(updates.getLocation());
		if (updates.getName() != null)
			e.setName(updates.getName());
		if (updates.getStartTimestamp() != null)
			e.setStartTimestamp(updates.getStartTimestamp());
		if (updates.getRelationship() != null)
			e.setRelationship(updates.getRelationship());
		
		return e;
	}
	
	public String toJson() {
		return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(this);
	}
	
	private void load() {
		if (id != 0) {
			Connection conn = null;
			try {
				conn = Util.getConn();
				PreparedStatement st = conn.prepareStatement("SELECT * FROM Events WHERE id=?");
				st.setLong(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					setName(rs.getString(2));
					setStartTimestamp(rs.getTimestamp(3));
					setEndTimestamp(rs.getTimestamp(4));
					setLocation(rs.getString(5));
					setDescription(rs.getString(6));
					setColor(rs.getString(7));
					setNotify(rs.getBoolean(8));
					loadSharedWith();
				} else {
					setId(0);
				}
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
		}
	}
	
	public boolean delete() {
		if (id != 0) {
			Connection conn = null;
			boolean deleted = false;
			try {
				conn = Util.getConn();
				PreparedStatement st = conn.prepareStatement("DELETE FROM Events WHERE id=?");
				st.setLong(1, id);
				deleted = (st.executeUpdate() != 0);
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
			return deleted;
		}
		return false;
	}
	
	public void write(User current_user) {
		Connection conn = null;
		try {
			conn = Util.getConn();
			conn.setAutoCommit(false);

			PreparedStatement st2 = null;
			ResultSet rs = null;
			boolean insert = false;
			if (id != 0) {
				PreparedStatement st = conn.prepareStatement("SELECT id FROM Events WHERE id=?");
				st.setLong(1, id);
				rs = st.executeQuery();
			}
			if (id != 0 && rs.next()) {
				// event exists, update
				st2 = conn.prepareStatement("UPDATE Events SET name=?, start_time=?, end_time=?, location=?, description=?, color=?, notify=? WHERE id=?");
				st2.setLong(8, id);
			} else {
				// event does not exist
				insert = true;
				st2 = conn.prepareStatement("INSERT INTO Events (name, start_time, end_time, location, description, color, notify) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}
			st2.setString(1, name);
			st2.setTimestamp(2, start_time);
			st2.setTimestamp(3, end_time);
			st2.setString(4, location);
			st2.setString(5, description);
			st2.setString(6, color);
			st2.setBoolean(7, notify);
			st2.executeUpdate();
			conn.commit();
			if (insert) {
				ResultSet keys = st2.getGeneratedKeys();
				if (keys.next()) {
					setId(keys.getLong(1));
				}
			}
			
			// write own relationship if noone owns this event
			st2 = conn.prepareStatement("SELECT 0 FROM EventRelationships WHERE event_id=? and relationship_type=?");
			st2.setLong(1, id);
			st2.setString(2, RelationshipType.OWNED);
			if (!st2.executeQuery().next()) 
				createRelationship(conn, current_user, RelationshipType.OWNED);

			// write shared relationships
			for (User u: shared) {
				createRelationship(conn, u, RelationshipType.SHARED);
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
		if (user != null) {
			try {
				PreparedStatement st = conn.prepareStatement("INSERT INTO EventRelationships VALUES (?, ?, ?)");
				st.setLong(1, user.getId());
				st.setLong(2, id);
				st.setString(3, type);
				st.executeUpdate();
			} catch (SQLException e) {
				// INSERT IF NOT EXISTS by virtue of unique constraint 
				if (e.getErrorCode() != 1062)
					throw e;
			}
		}
	}
	
	private void loadSharedWith() {
		try {
			Connection conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement("SELECT * FROM EventRelationships WHERE event_id=? AND relationship_type=?");
			st.setLong(1, id);
			st.setString(2, RelationshipType.SHARED);
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
		return start_time;
	}
	
	public Timestamp getEndTimestamp() {
		return end_time;
	}

	public void setStartTimestamp(Timestamp ts) {
		this.start_time = ts;
	}
	
	public void setEndTimestamp(Timestamp ts) {
		this.end_time = ts;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	
	/*
	 * Call Event.write(...) after to save
	 */
	public void addShared(User u) {
		shared.add(u);
		Notification n = new Notification(u.getId(), getId(), Notification.DEFAULT_TYPE);
		n.write();
	}
	
	/*
	 * Automatically saves the change to db
	 */
	public void removeShare(User u) {
		try {
			Connection conn = Util.getConn();
			PreparedStatement st = conn.prepareStatement("DELETE FROM EventRelationships WHERE user_id=? AND event_id=? AND relationship_type=?");
			st.setLong(1, u.getId());
			st.setLong(2, id);
			st.setString(3, RelationshipType.SHARED);
			if (st.executeUpdate() != 0) {
				shared.remove(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
