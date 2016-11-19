package usc_classes;

import java.sql.Time;

public class USCSection {
//	private int id = -1; 
	private int type; 
	private int section_id; 
	private long class_id;
	private String dept, location, name;
	private Time start_time, end_time; 
	private boolean mon, tue, wed, thur, fri; 
	private String description;
	
	protected USCSection() {}

	public USCSection(int section_id, long class_id, String dept, String location, String name, Time start_time, Time end_time, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			String description) {
		this.section_id = section_id;
		this.class_id = class_id;
		this.dept = dept; 
		this.location = location;
		this.name = name;
		setStart_time(start_time);
		setEnd_time(end_time);
		this.mon = mon;
		this.tue = tue;
		this.wed = wed;
		this.thur = thur;
		this.fri = fri;
		this.description = description;
		type = -1; 
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Time getStart_time() {
		return start_time;
	}

	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}

	public Time getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}

	public long getClass_id() {
		return class_id;
	}

	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}
	
	
}
