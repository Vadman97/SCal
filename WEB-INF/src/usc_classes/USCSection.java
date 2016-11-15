package usc_classes;

public class USCSection {
	private int id; 
	private int type; 
	private int section_id, class_id; 
	private String dept, location, name;
	private Time start, end; 
	private boolean mon, tue, wed, thur, fri; 
	private String description;
	private boolean isFullDay;

	public USCSection(int section_id, int class_id, String dept, String location, String name, Time start, Time end, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			String description, boolean isFullDay) {
		this.section_id = section_id;
		this.class_id = class_id;
		this.dept = dept; 
		this.location = location;
		this.name = name;
		this.start = start;
		this.end = end;
		this.mon = mon;
		this.tue = tue;
		this.wed = wed;
		this.thur = thur;
		this.fri = fri;
		this.description = description;
		this.isFullDay = isFullDay; 
		type = -1; 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	public boolean isMon() {
		return mon;
	}

	public void setMon(boolean mon) {
		this.mon = mon;
	}

	public boolean isTue() {
		return tue;
	}

	public void setTue(boolean tue) {
		this.tue = tue;
	}

	public boolean isWed() {
		return wed;
	}

	public void setWed(boolean wed) {
		this.wed = wed;
	}

	public boolean isThur() {
		return thur;
	}

	public void setThur(boolean thur) {
		this.thur = thur;
	}

	public boolean isFri() {
		return fri;
	}

	public void setFri(boolean fri) {
		this.fri = fri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFullDay() {
		return isFullDay;
	}

	public void setFullDay(boolean isFullDay) {
		this.isFullDay = isFullDay;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
