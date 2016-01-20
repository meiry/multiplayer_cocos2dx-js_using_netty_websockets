package com.server;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.json.JSONArray;
import com.json.JSONObject;

public class GameResponseDispatcher {
	private final static Logger LOG = LoggerManager.GetLogger(GameEventHandler.class.getName());
	private GameManager gameManager; 
	
	public GameResponseDispatcher(GameManager _gameManager)
	{
		this.gameManager = _gameManager;
		 
	}
	public boolean ResponseDispatcheLoginDone(int  _playerId)
	{
		int currentPlayerId = _playerId;
		Player currentPlayer = this.gameManager.getPlayers().get(currentPlayerId);
		
		JSONObject currentPlayerJsonObj = setPlayerToJson(currentPlayer,
												   currentPlayer.getEvent(),
												   currentPlayer.getId());
		
		JSONObject currentPlayerJsonObj2 = setPlayerToJson(currentPlayer,
											   currentPlayer.getEvent(),
											   currentPlayer.getId());
		 
		//build the other players json
    	JSONArray currentPlayerArrayNewPlayers = new JSONArray();
    	JSONArray ArrayCurrentPlayers = new JSONArray();
    	ArrayCurrentPlayers.put(currentPlayerJsonObj2);
    	
    	Iterator<Entry<Integer, Player>> it = this.gameManager.getPlayers().entrySet().iterator();
	    while (it.hasNext()) {
	    	@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        Player playerIt = ((Player)pair.getValue());
	         
	         
	        if(currentPlayerId != playerIt.getId())
	        {
	        	this.gameManager.getPlayers().get(playerIt.getId()).setEvent(Config.NEW_USER_LOGIN_DONE); 
	        	JSONObject playerJsonObjIt = setPlayerToJson(playerIt,
									        				Config.NEW_USER_LOGIN_DONE,
									        				playerIt.getId());
	        	JSONObject newPlayerForCurrent  = setPlayerToJson(playerIt,
										        				Config.NEW_USER_LOGIN_DONE,
										        				playerIt.getId());
	        	//to current
	        	//LOG.severe(newPlayerForCurrent.toString()); 	        	 
	        	currentPlayerArrayNewPlayers.put(newPlayerForCurrent);
	        	//LOG.severe(currentPlayerArrayNewPlayers.toString());
	        	//to others 
	        	playerJsonObjIt.put("players",ArrayCurrentPlayers);
	        	String jsonStr = playerJsonObjIt.toString();
	        	//LOG.severe("jsonStr in while:"+jsonStr);
	        	this.gameManager.getPlayers().get(playerIt.getId()).setPlayerJson(jsonStr);
	        	//LOG.severe("currentPlayerArrayNewPlayers:"+currentPlayerArrayNewPlayers.toString());
	        }
	        
	        
	    }
	    //current user array
	    //LOG.severe(currentPlayerArrayNewPlayers.toString());
	    currentPlayerJsonObj.put("players",currentPlayerArrayNewPlayers);
	    String jsonStr = currentPlayerJsonObj.toString();
	   // LOG.severe("jsonStr:"+jsonStr);
	  //  LOG.severe("getPlayerJson() BEFOR:"+this.gameManager.getPlayers().get(currentPlayerId).getPlayerJson());
	    this.gameManager.getPlayers().get(currentPlayerId).setPlayerJson(jsonStr);
	   // LOG.severe("getPlayerJson() AFTER:"+this.gameManager.getPlayers().get(currentPlayerId).getPlayerJson());
	    
	    
		return true;
	}
	
	public boolean ResponseDispatchePlayDone(int _playerId)
	{
		int currentPlayerId = _playerId;
		this.gameManager.getPlayers().get(currentPlayerId).setEvent(Config.PLAY_DONE);
		int newActivePlayer = this.gameManager.getNextActivePlayer(currentPlayerId);
		this.gameManager.getPlayers().get(currentPlayerId).setActiveplayerid(newActivePlayer);
		
		Player currentPlayer = this.gameManager.getPlayers().get(currentPlayerId);
		
		JSONObject currentPlayerJsonObj = setPlayerToJson(currentPlayer,
												   currentPlayer.getEvent(),
												   currentPlayer.getId());
		
		JSONObject currentPlayerJsonObj2 = setPlayerToJson(currentPlayer,
											   currentPlayer.getEvent(),
											   currentPlayer.getId());
		
		
		//build the other players json
    	JSONArray currentPlayerArrayNewPlayers = new JSONArray();
    	JSONArray ArrayCurrentPlayers = new JSONArray();
    	ArrayCurrentPlayers.put(currentPlayerJsonObj2);
    	
    	Iterator<Entry<Integer, Player>> it = this.gameManager.getPlayers().entrySet().iterator();
	    while (it.hasNext()) {
	    	@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        Player playerIt = ((Player)pair.getValue());
	         
	         
	        if(currentPlayerId != playerIt.getId())
	        {
	        	
	        	//update each user 
	        	this.gameManager.getPlayers().get(playerIt.getId()).setDeckcard(currentPlayer.getDeckcard());
	        	this.gameManager.getPlayers().get(playerIt.getId()).setEvent(Config.PLAY_DONE); 
	        	this.gameManager.getPlayers().get(playerIt.getId()).setActiveplayerid(newActivePlayer);
	        	
	        	JSONObject playerJsonObjIt = setPlayerToJson(playerIt,
									        				Config.PLAY_DONE,
									        				playerIt.getId());
	        	JSONObject newPlayerForCurrent  = setPlayerToJson(playerIt,
										        				Config.PLAY_DONE,
										        				playerIt.getId());
	        	
	        	 
	        	currentPlayerArrayNewPlayers.put(newPlayerForCurrent);	        	 
	        	//to others 
	        	playerJsonObjIt.put("players",ArrayCurrentPlayers);
	        	String jsonStr = playerJsonObjIt.toString();
	        	//LOG.severe("jsonStr in while:"+jsonStr);
	        	this.gameManager.getPlayers().get(playerIt.getId()).setPlayerJson(jsonStr);
	        	//LOG.severe("currentPlayerArrayNewPlayers:"+currentPlayerArrayNewPlayers.toString());
	        }
	        
	        
	    }
	    //current user array
	    //LOG.severe(currentPlayerArrayNewPlayers.toString());
	    currentPlayerJsonObj.put("players",currentPlayerArrayNewPlayers);
	    //String cardInDeck = this.gameManager.getCardsPlayDeck().getFirst();
	    //currentPlayerJsonObj.put("deck", cardInDeck);
	    String jsonStr = currentPlayerJsonObj.toString();
	   // LOG.severe("jsonStr:"+jsonStr);
	  //  LOG.severe("getPlayerJson() BEFOR:"+this.gameManager.getPlayers().get(currentPlayerId).getPlayerJson());
	    this.gameManager.getPlayers().get(currentPlayerId).setPlayerJson(jsonStr);
	   // LOG.severe("getPlayerJson() AFTER:"+this.gameManager.getPlayers().get(currentPlayerId).getPlayerJson());
	   
		return true;
	}
	
	@SuppressWarnings("unused")
	private JSONObject setPlayerToJson(final Player _player,final int event,
										final int _playerId)
	{	
		JSONObject _jsonPlayer = new JSONObject();
		_jsonPlayer.put("id",_playerId);
		_jsonPlayer.put("event",event);
		_jsonPlayer.put("username",_player.getUserName());
		_jsonPlayer.put("activecardid",_player.getActivecardid());
		_jsonPlayer.put("activeplayerid",_player.getActiveplayerid()); 
		_jsonPlayer.put("registertionnum",_player.getRegistertionNum()); 
		_jsonPlayer.put("winner",_player.getWinner()); 
		_jsonPlayer.put("deck",_player.getDeckcard()); 
		_jsonPlayer.put("endgame",_player.getEndgame()); 
		_jsonPlayer.put("winnercards",_player.getWinnercards()); 
		_jsonPlayer.put("numcardsleft",_player.getPlayerCards().size()); 
		
		 
		return _jsonPlayer;
	}
}
