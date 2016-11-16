package usc_classes;

import java.io.Serializable;

public class Time implements Serializable {
	
	private static final long serialVersionUID = 1; 
	private int hour, minute;

	public Time(int hour, int minute) {
		this.hour = hour; 
		this.minute = minute; 
	}
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
}