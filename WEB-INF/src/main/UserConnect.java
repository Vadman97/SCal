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

import user.User;

@ServerEndpoint(value="/userConnect", configurator=WebsocketConfiguration.class)
public class UserConnect {

    private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
    private static Map<User, HttpSession> userToSession = new HashMap<>();//username and HttpSession
    private static String username;
    private Session session;
    private HttpSession httpSession;


    public UserConnect(){
    }
    @OnOpen
        public void open(Session session, EndpointConfig config) {
            this.session = session;
            this.httpSession = (HttpSession) config.getUserProperties().get("httpSession");
            allUsers.add(this);
            System.out.println(httpSession.getAttribute("user"));
            User curr = (User)httpSession.getAttribute("user");
            userToSession.put(curr, this.httpSession);
        }

    @OnClose
        public void close(Session session) {
            allUsers.remove(this);
        }

    @OnError
        public void onError(Throwable error) throws Throwable{
            System.out.println("Error: " + error.toString());
        }

    @OnMessage //when someone sends msg in test mode, print all users and sessions
        public void msgHandle(String message) {
            System.out.println(message);
            for(Map.Entry<User, HttpSession> userEntry: userToSession.entrySet()){
            	System.out.println(userEntry.getKey().toString() + "| " + userEntry.getValue().getId());
            }
        }
}