package usc_classes;

import java.sql.Timestamp;

import main.Constants;

public class Discussion extends USCSection {

	private int discussion_id; 
	
	public Discussion(int section_id, int class_id, String dept, String location, String name, Timestamp start_time, Timestamp end_time, boolean mon, boolean tue,
			boolean wed, boolean thur, boolean fri, String description, int discussion_id) {
		super(section_id, dept, location, name, start_time, end_time, mon, tue, wed, thur, fri, description);
		
		this.discussion_id = discussion_id; 
		setType(Constants.discussion);
	}

	public int getDiscussion_id() {
		return discussion_id;
	}

	public void setDiscussion_id(int discussion_id) {
		this.discussion_id = discussion_id;
	}
	
}
