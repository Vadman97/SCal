package backend;

public class Lab extends USCSection {
	
	private int lab_id;

	public Lab(int section_id, int class_id, String dept, String location, String name, Time start, Time end, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			String description, boolean isFullDay, int lab_id) {
		super(section_id, class_id, dept, location, name, start, end, mon, tue, wed, thur, fri, description, isFullDay);
		
		this.lab_id = lab_id; 
		setType(Constants.lab);
		
	}

	public int getLab_id() {
		return lab_id;
	}

	public void setLab_id(int lab_id) {
		this.lab_id = lab_id;
	}

}
