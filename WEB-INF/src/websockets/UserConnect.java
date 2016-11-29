package websockets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;

import calendar.Calendar;
import calendar.Event;
import user.User;

@ServerEndpoint(value = "/userConnect", configurator = WebsocketConfiguration.class)
public class UserConnect implements Runnable{

	private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
	private static Map<User, Session> userToSession = new ConcurrentHashMap<>();
	private Session session;
	private HttpSession httpSession;
	private volatile boolean running = false;

	public UserConnect() {}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		if (!running) {
			running = true;
			new Thread(this).start();
		}
		
		this.session = session;
		this.httpSession = (HttpSession) config.getUserProperties().get("httpSession");
		allUsers.add(this);
		User curr = (User) httpSession.getAttribute("user");
		if (curr != null) {
			if (userToSession.containsKey(curr))
				close(userToSession.get(curr));
			userToSession.put(curr, this.session);
		}
	}

	@OnClose
	public void close(Session session) {
		allUsers.remove(this);
		// remove that user from the hashmap of user to sessions
		for (Map.Entry<User, Session> entry : userToSession.entrySet()) {
			if (session.getId().equals(entry.getValue().getId())) {
				User tmp = entry.getKey();
				userToSession.remove(tmp);
			}
		}
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (allUsers.size() == 0)
			running = false;
	}

	@OnError
	public void onError(Throwable error) throws Throwable {
		System.out.println("Error: " + error.toString());
	}

	public static void sendJSONtoClient(User toReceive, String eventJSON) {
		try {
			if (userToSession.get(toReceive) != null) {
				userToSession.get(toReceive).getBasicRemote().sendText(eventJSON);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			for (Entry<User, Session> e: userToSession.entrySet()) {
				Calendar c = new Calendar(e.getKey());
				c.getAll(e.getKey());
				ArrayList<Event> events = c.events;
				if (events.size() == 0)
					continue;
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				Date now = cal.getTime();
				for (Event ev: events) {
					cal.setTime(ev.getStartTimestamp());
					cal.add(java.util.Calendar.MINUTE, -30);
					if (now.after(cal.getTime())) {
						cal.setTime(ev.getEndTimestamp());
						if (now.before(cal.getTime())) {
							JsonObject event = ev.toJsonObj();
							event.addProperty("eventAlert", true);
							try {
								e.getValue().getBasicRemote().sendText(event.toString());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {}
		}
	}
}