package backend;

public class USCClass extends USCSection {

	private int class_num; 
	private int semester; 
	private String professor; 
	
	public USCClass(int section_id, int class_id, String dept, String location, String name, Time start, Time end, 
			boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, 
			int semester, String professor, String description, boolean isFullDay, int class_num) {
		super(section_id, class_id, dept, location, name, start, end, mon, tue, wed, thur, fri, description, isFullDay);
		
		this.class_num = class_num; 
		this.semester = semester; 
		this.professor = professor; 
		setType(Constants.classSection);
	}

	public int getClass_num() {
		return class_num;
	}

	public void setClass_num(int class_num) {
		this.class_num = class_num;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}
	
	
	
}
