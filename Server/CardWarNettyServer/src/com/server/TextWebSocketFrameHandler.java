package com.server;

 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.json.JSONObject;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
 
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;

//@Sharable 
public class TextWebSocketFrameHandler  extends SimpleChannelInboundHandler<TextWebSocketFrame>{
		//private static final AttributeKey<GameManager> GameManagerAttribute = AttributeKey.valueOf("gameManager");
		private final static Logger LOG = LoggerManager.GetLogger(GameServerMain.class.getName());	     
	    private  GameManager gameManager;
	    private  GameEventHandler gameEventHandler;

	    public TextWebSocketFrameHandler(GameManager _gameManager,GameEventHandler _gameEventHandler) {
	        this.gameManager = _gameManager;
	        this.gameEventHandler = _gameEventHandler;
	         
	    }
	    public TextWebSocketFrameHandler()
	    {
	         ;
	    }

	    @Override
	    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
 
	    	
	        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {

	            ctx.pipeline().remove(HttpRequestHandler.class);
	            JSONObject responseJson = new JSONObject();
				responseJson.put("event",Config.HANDSHAKE_COMPLETE_SUCCESS);				 
				LOG.severe("HANDSHAKE_COMPLETE "+responseJson.toString());
	            ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson.toString()));
	            
	           
	        } else {
	            super.userEventTriggered(ctx, evt);
	        }
	    }

	    @Override
	    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
	    	msg.retain();
	    	TextWebSocketFrame frame = (TextWebSocketFrame) msg;
	    	String jsonRequest = frame.text(); 
	    	LOG.severe("Recived from client :"+jsonRequest); 
	        try {	        	
	        	 
	        	int playerId = this.gameEventHandler.handleEvent(jsonRequest,ctx.channel());
	        	if(playerId !=-1)
	        	{
	        		this.gameEventHandler.ResponseDispatcher(playerId,jsonRequest);
	        		Iterator<Entry<Integer, Player>> it = this.gameManager.getPlayers().entrySet().iterator();
	        	    while (it.hasNext()) {
	        	    	@SuppressWarnings("rawtypes")
	        			Map.Entry pair = (Map.Entry)it.next();
	        	        Player playerIt = ((Player)pair.getValue());
	        	        String PlayerMassage = playerIt.getPlayerJson().toString();
	        	        responseToClient(playerIt,PlayerMassage);
	        	        LOG.severe("Sending to client id:"+String.valueOf(playerIt.getId())+" name:"+playerIt.getUserName()+" json:" +PlayerMassage);
	        	    }
	        	}
	        	else
	        	{
	        		LOG.severe("Sending to clients Failed playerId is -1");
	        	}
	            
	        } finally {
	            frame.release();
	        }
	    }
	    
	    private void responseToClient(Player _player,String responseJson)
		{
			_player.getChannel().writeAndFlush(new TextWebSocketFrame(responseJson));
		}
}
