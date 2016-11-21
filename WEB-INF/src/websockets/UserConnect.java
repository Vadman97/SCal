package websockets;

import java.io.IOException;
import java.util.Map;
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

import user.User;

@ServerEndpoint(value = "/userConnect", configurator = WebsocketConfiguration.class)
public class UserConnect {

	private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
	private static Map<User, Session> userToSession = new ConcurrentHashMap<>();
	private Session session;
	private HttpSession httpSession;

	public UserConnect() {}

	@OnOpen
	public void open(Session session, EndpointConfig config) {
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
}