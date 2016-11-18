package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import main.WebsocketConfiguration;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.User;

@ServerEndpoint(value="/userConnect", configurator=WebsocketConfiguration.class)
public class UserConnect {

    private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
    private static Map<User, Session> userToSession = new ConcurrentHashMap<>();//username and Session
    private Session session;
    private HttpSession httpSession;


    public UserConnect(){
    }
    @OnOpen
        public void open(Session session, EndpointConfig config) {
            this.session = session;
            this.httpSession = (HttpSession) config.getUserProperties().get("httpSession");
            allUsers.add(this);
            User curr = (User)httpSession.getAttribute("user");
            userToSession.put(curr, this.session);
        }

    @OnClose
        public void close(Session session) {
            allUsers.remove(this);
        }

    @OnError
        public void onError(Throwable error) throws Throwable{
            System.out.println("Error: " + error.toString());
        }

    	public static void sendJSONtoClient(User toReceive, String eventJSON){
    		//TODO send a json to the specific client based on info in a json received
            if(!userToSession.isEmpty()){
            	try{
    				userToSession.get(toReceive).getBasicRemote().sendText(eventJSON);
    			} catch(IOException ioe){
    				ioe.printStackTrace();
    			}
            }
    	}
}