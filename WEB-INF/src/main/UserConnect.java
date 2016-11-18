package main;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.EndpointConfig;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;
import main.WebsocketConfiguration;

@ServerEndpoint(value="/userConnect", configurator=WebsocketConfiguration.class)
public class UserConnect {

    private static final AtomicInteger connNum = new AtomicInteger(0);
    private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
    private static Map<String, String> userToID = new HashMap<>();//username and cookie
    private static String username;
    private Session session;
    private HttpSession httpSession;


    public UserConnect(){
        username = "Name"+connNum.getAndIncrement();
    }
    @OnOpen
        public void open(Session session, EndpointConfig config) {
            this.session = session;
            this.httpSession = (HttpSession) config.getUserProperties().get("httpSession");
            allUsers.add(this);
        }

    @OnClose
        public void close(Session session) {
            allUsers.remove(this);
        }

    @OnError
        public void onError(Throwable error) throws Throwable{
            System.out.println("Error: " + error.toString());
        }

    @OnMessage //the message is the username::cookie
        public void msgHandle(String message) {
            System.out.println(message);
            System.out.print(httpSession);
            System.out.println(httpSession.getAttribute("user"));
            String[] contents = message.split("::");
            userToID.put(contents[0], contents[1]);
        }
}