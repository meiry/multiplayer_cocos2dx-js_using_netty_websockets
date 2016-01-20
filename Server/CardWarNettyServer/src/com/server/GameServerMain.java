package com.server;

import java.util.ResourceBundle;
import java.util.logging.Logger;
//http://comoyo.github.io/blog/2012/07/30/integrating-websockets-in-netty/
//http://kevinwebber.ca/multiplayer-tic-tac-toe-in-java-using-the-websocket-api-netty-nio-and-jquery/
public class GameServerMain {
	private final static Logger LOG = LoggerManager.GetLogger(GameServerMain.class.getName());
	public static void main(String[] args) {
		 
		final ResourceBundle configurationBundle = ResourceBundle.getBundle("configuration");
		int port = Integer.valueOf(configurationBundle.getString("port"));
		WebSocketServer pWebSocketServer = new WebSocketServer(); 
		pWebSocketServer.start(port);
		LOG.info("server started");
		
	}

}
