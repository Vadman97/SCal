package calendar;

import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

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
	
	public static Set<Event>importICS(Reader fis){
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
				if(p.toString().contains(p.UID)){
					String tmp = "0";
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
				if(p.toString().contains(p.DESCRIPTION)){
					String tmp = p.toString();
					tmp = tmp.substring(12, tmp.length());
					description = tmp;
				}
				if(p.toString().contains(p.SUMMARY)){
					String tmp = p.toString();
					tmp = tmp.substring(8, tmp.length());
					name = tmp;
				}
				if(p.toString().contains(p.LOCATION)){
					String tmp = p.toString();
					tmp = tmp.substring(9, tmp.length());
					location = tmp;
				}
			}
			System.out.println(id);
			System.out.println(name);
			System.out.println(startDate);
			System.out.println(endDate);
			System.out.println(location);
			System.out.println(description);
			Event e = new Event(id, name, startDate, endDate, location, description, "#ff0000", false, "owned");
			eventsExtractedFromICS.add(e);
		}
		return eventsExtractedFromICS;
	}
	
	

	public static String timestampToICSDate(Timestamp t){
		String timestamp = t.toString();
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
	
	public static int getTimestampYEARFromEvent(Timestamp t){
		long timestamp = t.getTime();
		java.util.Calendar startTimeCal = java.util.Calendar.getInstance();
		startTimeCal.setTimeInMillis(timestamp);
		return startTimeCal.get(java.util.Calendar.YEAR);
	}
	public static int getTimestampMONTHFromEvent(Timestamp t){
		long timestamp = t.getTime();
		java.util.Calendar startTimeCal = java.util.Calendar.getInstance();
		startTimeCal.setTimeInMillis(timestamp);
		return startTimeCal.get(java.util.Calendar.MONTH);
	}
	public static int getTimestampDAYFromEvent(Timestamp t){
		long timestamp = t.getTime();
		java.util.Calendar startTimeCal = java.util.Calendar.getInstance();
		startTimeCal.setTimeInMillis(timestamp);
		return startTimeCal.get(java.util.Calendar.DAY_OF_MONTH);
	}
	public static int getTimestampHOURFromEvent(Timestamp t){
		long timestamp = t.getTime();
		java.util.Calendar startTimeCal = java.util.Calendar.getInstance();
		startTimeCal.setTimeInMillis(timestamp);
		return startTimeCal.get(java.util.Calendar.HOUR_OF_DAY);
	}
	public static int getTimestampMINUTEFromEvent(Timestamp t){
		long timestamp = t.getTime();
		java.util.Calendar startTimeCal = java.util.Calendar.getInstance();
		startTimeCal.setTimeInMillis(timestamp);
		return startTimeCal.get(java.util.Calendar.MINUTE);
	}
	
	public static String exportICS(ArrayList<Event> listOfEvents){
		
		Calendar cal = new Calendar();
		cal.getProperties().add(new ProdId("-//SCal Team//SCal 1.0//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);
		
		for(Event e : listOfEvents){
			TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
			TimeZone timezone = registry.getTimeZone("America/Los_Angeles");
			VTimeZone tz = timezone.getVTimeZone();
		
			
			java.util.Calendar startDate = new GregorianCalendar();
			startDate.setTimeZone(timezone);
			startDate.set(java.util.Calendar.MONTH, getTimestampMONTHFromEvent(e.getStartTimestamp()));
			startDate.set(java.util.Calendar.DAY_OF_MONTH, getTimestampDAYFromEvent(e.getStartTimestamp()));
			startDate.set(java.util.Calendar.YEAR, getTimestampYEARFromEvent(e.getStartTimestamp()));
			startDate.set(java.util.Calendar.HOUR_OF_DAY, getTimestampHOURFromEvent(e.getStartTimestamp()));
			startDate.set(java.util.Calendar.MINUTE, getTimestampMINUTEFromEvent(e.getStartTimestamp()));
			startDate.set(java.util.Calendar.SECOND, 0);
			
			java.util.Calendar endDate = new GregorianCalendar();
			endDate.setTimeZone(timezone);
			endDate.set(java.util.Calendar.MONTH, getTimestampMONTHFromEvent(e.getEndTimestamp()));
			endDate.set(java.util.Calendar.DAY_OF_MONTH, getTimestampDAYFromEvent(e.getEndTimestamp()));
			endDate.set(java.util.Calendar.YEAR, getTimestampYEARFromEvent(e.getEndTimestamp()));
			endDate.set(java.util.Calendar.HOUR_OF_DAY, getTimestampHOURFromEvent(e.getEndTimestamp()));
			endDate.set(java.util.Calendar.MINUTE, getTimestampMINUTEFromEvent(e.getEndTimestamp()));	
			endDate.set(java.util.Calendar.SECOND, 0);
			
			String eventName = e.getName();
			DateTime start = new DateTime(startDate.getTime());
			DateTime end = new DateTime(endDate.getTime());
			VEvent event = new VEvent(start, end, eventName);
			
			event.getProperties().add(tz.getTimeZoneId());
			
			UidGenerator ug = null;
			try {
				ug = new UidGenerator("uidGen");
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Uid uid = ug.generateUid();
			event.getProperties().add(uid);

			cal.getComponents().add(event);
			
		}
		
		return cal.toString();
				
//		File out = new File("output.ics");
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(out);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//		try {
//			bw.write(cal.toString());
//		} catch (IOException e1) {
//			System.out.println("Error writing to file: "+ e1.getMessage());
//		}
//		try {
//			bw.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
	}	
}