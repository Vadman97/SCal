package backend;

public class Exam {

	private int exam_id, class_id;
	private int type; 
	Time start, end;
	
	public Exam(int exam_id, int class_id, Time start, Time end) {
		this.exam_id = exam_id;
		this.class_id = class_id;
		this.start = start;
		this.end = end;
		type = Constants.exam;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getClass_id() {
		return class_id;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
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
	
	
}
