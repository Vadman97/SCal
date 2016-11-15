package backend;

public class Discussion extends USCSection {

	private int discussion_id; 
	
	public Discussion(int section_id, int class_id, String dept, String location, String name, Time start, Time end, boolean mon, boolean tue,
			boolean wed, boolean thur, boolean fri, String description, boolean isFullDay, int discussion_id) {
		super(section_id, class_id, dept, location, name, start, end, mon, tue, wed, thur, fri, description, isFullDay);
		
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
