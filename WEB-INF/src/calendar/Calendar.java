package calendar;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Calendar {

	Queue<Event> upcoming;
	Queue<Event> past;
	
	public Calendar() {
		upcoming = new ArrayBlockingQueue<Event>(Integer.MAX_VALUE);
	}
}
