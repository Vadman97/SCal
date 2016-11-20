package main;

import javax.websocket.*;
import javax.servlet.*;
import javax.websocket.server.*;
import javax.servlet.http.*;

public class WebsocketConfiguration extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("httpSession", httpSession);
    }

}