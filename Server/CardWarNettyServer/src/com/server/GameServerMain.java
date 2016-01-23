package com.server;

import java.util.ResourceBundle;
import java.util.logging.Logger;
 
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
