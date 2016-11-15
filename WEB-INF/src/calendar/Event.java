package calendar;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import user.User;
import util.Util;
//import java.util.Date;

public class Event {
	 
	private long id;
	private String name;
	private Timestamp datetime; 
	private String location, description; 
	private Color color; 
	private boolean notify;
	
	private Vector<User> shared;
	
	public Event() {
		shared = new Vector<User>();
	}
	
	// used from Calendar when loading all events from db
	public Event(long id, String name, Timestamp datetime, String location, String description, Color color, boolean notify) {
		this();
		setId(id);
		setName(name);
		setDatetime(datetime);
		setLocation(location);
		setDescription(description);
		setColor(color);
		setNotify(notify);
		loadSharedWith();
	}

	public void parse(String json) {
		
	}
	
	public void write() {
		
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

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
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
