package calendar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.sql.Timestamp;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

public class icsHandler {
	private static Timestamp ICSToTimestamp(String icsDate){
		
		icsDate = icsDate.replace("00Z", "");
		int YYYY = Integer.parseInt(icsDate.substring(0, 4));
		int MM = Integer.parseInt(icsDate.substring(4, 6));
		int DD = Integer.parseInt(icsDate.substring(6, 8));
		int HH = Integer.parseInt(icsDate.substring(9, 11));
		HH-=8;
		int mm = Integer.parseInt(icsDate.substring(11, 13));
		int ss = 0;
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(YYYY, MM, DD, HH, mm, ss);
		Timestamp toReturn = new Timestamp(cal.getTime().getTime());
		return toReturn;
		
	}
	
	public static Set<Event>importICS(FileInputStream fis){
		Set<Event> eventsExtractedFromICS = new HashSet<>();
		Calendar cal = null;
		CalendarBuilder cb = new CalendarBuilder();
		try{
			cal = cb.build(fis);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(ParserException pe){
			pe.printStackTrace();
		}
		//grabs all events from the ics file
		ComponentList<CalendarComponent> cl = cal.getComponents();
		for(CalendarComponent cc : cl){
			Timestamp startDate = null;
			Timestamp endDate = null;
			Long id = null;
			String description = "";
			String name = "";
			String location = "";
			for(Property p: cc.getProperties()){
				if(p.toString().contains(Property.UID)){
					String tmp = p.toString();
					tmp = tmp.substring(4, tmp.length()-2);
					id = Long.parseLong(tmp);
				}
				if(p.toString().contains("DTSTART")){
					String tmp = p.toString();
			        tmp = tmp.substring(8, tmp.length());
					startDate = ICSToTimestamp(tmp);
				}
				if(p.toString().contains("DTEND")){
					String tmp = p.toString();
			        tmp = tmp.substring(6, tmp.length());
					endDate = ICSToTimestamp(tmp);
				}
				if(p.toString().contains(Property.DESCRIPTION)){
					String tmp = p.toString();
					tmp = tmp.substring(12, tmp.length());
					description = tmp;
				}
				if(p.toString().contains(Property.SUMMARY)){
					String tmp = p.toString();
					tmp = tmp.substring(8, tmp.length());
					name = tmp;
				}
				if(p.toString().contains(Property.LOCATION)){
					String tmp = p.toString();
					tmp = tmp.substring(9, tmp.length());
					location = tmp;
				}
			}
			Event e = new Event(id, name, startDate, endDate, location, description, "red", false, "owned");
			eventsExtractedFromICS.add(e);
		}
		return eventsExtractedFromICS;
	}
	
	
	public void createICSFromEvents(Vector<Event> listOfEvents){
		Calendar cal = new Calendar();
		cal.getProperties().add(new ProdId("-//SCal Team//SCal 1.0//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		
		for(Event e : listOfEvents){
			
		}
		
		
//		File out = new File("output.ics");
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(out);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//		try{
//			bw.write(BEGINCAL);
//			bw.newLine();
//			bw.write(PRODID);
//			bw.newLine();
//			bw.write(VERSION);
//			bw.newLine();
//			bw.write(CALSCALE);
//			bw.newLine();
//			bw.write(METHOD);
//			bw.newLine();
//			bw.write(USEREMAIL);
//			bw.newLine();
//			bw.write(TIMEZONE);
//			bw.newLine();
//		}catch(IOException ioe){
//			ioe.printStackTrace();
//		}
		
//		
//		for(Event e: listOfEvents){
//			try{
//				bw.write(BEGINEVENT);
//				bw.newLine();
//				bw.write("DTSTART:"+timestampToICSDate(e.getStartTimestamp().toString()));
//				bw.newLine();
//				bw.write("DTEND:"+timestampToICSDate(e.getEndTimestamp().toString()));
//				bw.newLine();
//				bw.write("DTSTAMP:"+timestampToICSDate(e.getStartTimestamp().toString()));
//				bw.newLine();
//				bw.write(placeholderUID func);
//				bw.newLine();
//				bw.write("CREATED:"+timestampToICSDate(e.getStartTimestamp().toString()));
//				bw.newLine();
//				bw.write("DESCRIPTION:"+e.getDescription());
//				bw.newLine();
//				bw.write("LAST-MODIFIED:"+timestampToICSDate(e.getStartTimestamp().toString()));
//				bw.newLine();
//				bw.write("LOCATION:"+e.getLocation());
//				bw.newLine();
//				bw.write(SEQUENCE);
//				
//			}catch(IOException ioe){
//				ioe.printStackTrace();
//			}
//		}
		
	}
	public static String timestampToICSDate(String timestamp){
		//timestamp in format "YYYY-MM-DD HH:MM:SS"
		String UTCEND = "00Z";
        String[] data = timestamp.split(" ");
        data[0] = data[0].replace("-", "");
        data[1] = data[1].substring(0, Math.min(data[1].length(), 5));
        data[1] = data[1].replace(":", "");
        data[1] += UTCEND;
        String icsDateFormat = data[0]+data[1];
        return icsDateFormat;
	}
		
}