package calendar;

public class EventRelationships {
	private int user_id;
	private int event_id;
	private int relationship; 
	
	public EventRelationships(int user_id, int event_id, int relationship) {
		this.user_id = user_id;
		this.event_id = event_id;
		this.relationship = relationship;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public int getRelationship() {
		return relationship;
	}

	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}
	
	
}
