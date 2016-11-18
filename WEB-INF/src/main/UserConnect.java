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
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import main.WebsocketConfiguration;

import user.User;

@ServerEndpoint(value="/userConnect", configurator=WebsocketConfiguration.class)
public class UserConnect {

    private static Set<UserConnect> allUsers = new CopyOnWriteArraySet<>();
    private static Map<User, HttpSession> userToSession = new HashMap<>();//username and HttpSession
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
            if(!userToSession.isEmpty()){
            	for(Map.Entry<User, HttpSession> userEntry: userToSession.entrySet()){
                	System.out.println(userEntry.getKey().toString() + "| " + userEntry.getValue().getId());
                }	
            }
        }

    @OnClose
        public void close(Session session) {
            allUsers.remove(this);
        }

    @OnError
        public void onError(Throwable error) throws Throwable{
            System.out.println("Error: " + error.toString());
        }

    @OnMessage
        public void msgHandle(String message) {
            System.out.println(message);
        	for (UserConnect usr: allUsers) {
                try {
                    synchronized (usr) {
                        usr.session.getBasicRemote().sendText(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //connections.remove(client);
                }
            }	
        }
    	public void sendJSONtoClient(Session receiverSession, Session senderSession){
    		//TODO send a json to the specific client based on info in a json received
            if(!userToSession.isEmpty() && receiverSession!=null){
            	for(Map.Entry<User, HttpSession> userEntry: userToSession.entrySet()){
            		if(userEntry.getKey().equals(receiverSession.getId())){
            			try{
            				receiverSession.getBasicRemote().sendObject("");
            			}catch(EncodeException ee){
            				ee.printStackTrace();
            			}catch(IOException ioe){
            				ioe.printStackTrace();
            			}
            		}
                }	
            }
    	}
}