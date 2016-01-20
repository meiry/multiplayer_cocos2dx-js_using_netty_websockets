package com.server;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.json.JSONArray;
import com.json.JSONObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
//this class will handle all the request / response logic and game protocol 
public class GameEventHandler {
	private final static Logger LOG = LoggerManager.GetLogger(GameEventHandler.class.getName());
	private GameManager gameManager; 
	private static int playerIdCounter = 0;
	private static int playerRegistretionCounter = 0;
	public GameEventHandler(GameManager _gameManager)
	{
		this.gameManager = _gameManager;
		 
	}
	
	public int handleEvent(String _jsonRequest,Channel channel)
	{
		JSONObject jsonObject = new JSONObject(_jsonRequest);
		int Event = jsonObject.getInt("event");
		int playerId = -1;
		String userName =  jsonObject.getString("username");	        	
    	switch(Event)
    	{
        	case Config.LOGIN: 
        	{		        		 
        		Player newPlayer = setPlayerNewAttributes(userName,channel,Config.LOGIN_DONE);
        		setPlayerInPlayersContainer(newPlayer);
        		playerId = newPlayer.getId();
        		break;
        	}
        	case Config.PLAY:
        	{
        		playerId = invokePlayEvent(jsonObject);
        		
        	}
    	}
	        	
	    return playerId;
 	}
	
	
	public boolean ResponseDispatcher(int _playerId,String _jsonRequest)
	{
		JSONObject jsonObject = new JSONObject(_jsonRequest);
		int Event = jsonObject.getInt("event");
		boolean bDone = false;
		switch(Event)
    	{
        	case Config.LOGIN: 
        	{		        		 
        		bDone = this.gameManager.getGameResponseDispatcher().ResponseDispatcheLoginDone(_playerId);
        		break;
        	}
        	case Config.PLAY:
        	{
        		bDone = this.gameManager.getGameResponseDispatcher().ResponseDispatchePlayDone(_playerId); 
        		break;
        	}
    	}
		
		return bDone;
	}
	
	
	
	 
	private int invokePlayEvent(JSONObject _jsonObject)
	{
		int activePlayerId  = _jsonObject.getInt("id");
		int currentPlayerID = this.gameManager.getPlayers().get(activePlayerId).getActiveplayerid();
		//validation of turn
		if(activePlayerId==currentPlayerID)
		{
			
			//find out who is the previous player
			int playerInx = getPreviousePlayerIndex(currentPlayerID);
			
			
			String currentPlayerCardId = this.gameManager.getPlayers().get(activePlayerId).getActivecardid();
			//check if the cards deck is active in there are cards in it
			if(this.gameManager.getCardsPlayDeck().size()>0)
			{
				String prevCardId = this.gameManager.getCardsPlayDeck().getFirst();
				//check which card has greater value
				int  prevCardValue = this.gameManager.getCardValueById(prevCardId);
				int  currentCardValue = this.gameManager.getCardValueById(currentPlayerCardId);
				//check if previous card is greater
				if(prevCardValue > currentCardValue)
				{
					
					//set the cards to the winner which is previous player
					this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().addLast(currentPlayerCardId);
					this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().addLast(prevCardId);
					//set as winner
					this.gameManager.getPlayerByIndex(playerInx).setWinner(playerInx);
					this.gameManager.getPlayerByIndex(playerInx).setWinnercards(currentPlayerCardId+"_"+prevCardId);
					this.gameManager.getPlayerByIndex(currentPlayerID).setWinner(playerInx);
					this.gameManager.getPlayerByIndex(currentPlayerID).setWinnercards(currentPlayerCardId+"_"+prevCardId);
					
					String currentCartId = this.gameManager.getPlayers().get(activePlayerId).getPlayerCards().getFirst();
					this.gameManager.getPlayers().get(activePlayerId).setActivecardid(currentCartId);
					
					String cardInDeck = this.gameManager.getCardsPlayDeck().getFirst();
					this.gameManager.getPlayerByIndex(playerInx).setDeckcard(cardInDeck);
					this.gameManager.getCardsPlayDeck().clear(); 
					
				}
				//check if current card is greater
				else if(prevCardValue < currentCardValue)
				{
					 
					 
					String prevPlayerCardId = this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().getFirst();
					this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().removeFirst();
					this.gameManager.getPlayers().get(currentPlayerID).getPlayerCards().addLast(prevPlayerCardId);
					
					//set as winner
					this.gameManager.getPlayerByIndex(playerInx).setWinner(playerInx);
					this.gameManager.getPlayerByIndex(playerInx).setWinnercards(currentPlayerCardId+"_"+prevPlayerCardId);
					this.gameManager.getPlayerByIndex(currentPlayerID).setWinner(playerInx);
					this.gameManager.getPlayerByIndex(currentPlayerID).setWinnercards(currentPlayerCardId+"_"+prevPlayerCardId);
					
					
					String currentCartId = this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().getFirst();
					this.gameManager.getPlayerByIndex(playerInx).setActivecardid(currentCartId);
					
					String cardInDeck = this.gameManager.getCardsPlayDeck().getFirst();
					this.gameManager.getPlayerByIndex(playerInx).setDeckcard(cardInDeck);
					this.gameManager.getCardsPlayDeck().clear();
					
					
				}
				else if(prevCardValue == currentCardValue)
				{
					 
					String PreviousePlayerCards[] = getWarCards(playerInx);
					String currentPlayerCards[] = getWarCards(currentPlayerID); 
					
					int  prevCardValue_4 = this.gameManager.getCardValueById(PreviousePlayerCards[3]);
					int  currentCardValue_4 = this.gameManager.getCardValueById(currentPlayerCards[3]);
					//check who is the winner 
					if(prevCardValue_4 > currentCardValue_4)
					{
						String result = CardsArrayToString(PreviousePlayerCards,currentPlayerCards);
						this.gameManager.getPlayerByIndex(playerInx).setWinner(1);
						this.gameManager.getPlayerByIndex(playerInx).setWinnercards(result);
						String currentCartId = this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().getFirst();
						this.gameManager.getPlayerByIndex(playerInx).setActivecardid(currentCartId);
						 
					}
					else if(prevCardValue_4 < currentCardValue_4)
					{
						String result = CardsArrayToString(currentPlayerCards,PreviousePlayerCards);
						this.gameManager.getPlayerByIndex(currentPlayerID).setWinner(1);
						this.gameManager.getPlayerByIndex(currentPlayerID).setWinnercards(result);
						String currentCartId = this.gameManager.getPlayerByIndex(currentPlayerID).getPlayerCards().getFirst();
						this.gameManager.getPlayerByIndex(currentPlayerID).setActivecardid(currentCartId);
					}
					else if(prevCardValue_4 == currentCardValue_4)
					{
						//TODO 
						int test =0;
					}
					this.gameManager.getCardsPlayDeck().clear();
				}
			}
			else
			{
				this.gameManager.getCardsPlayDeck().addFirst(currentPlayerCardId);
				this.gameManager.getPlayers().get(activePlayerId).getPlayerCards().removeFirst();
				String currentCartId = this.gameManager.getPlayers().get(activePlayerId).getPlayerCards().getFirst();
				this.gameManager.getPlayers().get(activePlayerId).setActivecardid(currentCartId);
				
				String cardInDeck = this.gameManager.getCardsPlayDeck().getFirst();
				this.gameManager.getPlayers().get(activePlayerId).setDeckcard(cardInDeck);
	        	 
				
			}	
			
			//Check if there are winners for this game
			int prevPlayerCardsSize = this.gameManager.getPlayerByIndex(playerInx).getPlayerCards().size();
			if(prevPlayerCardsSize==0)
			{
				//game is ended
				this.gameManager.getPlayerByIndex(playerInx).setEndgame(currentPlayerID);
				this.gameManager.getPlayerByIndex(currentPlayerID).setEndgame(currentPlayerID);
				
			}
		}
		else
		{
			activePlayerId =-1;
		}
		return activePlayerId;
	}
	private String CardsArrayToString(String[] cardsPrev,String[] cardsCurrent)
	{
		String result ="";
		for (String s: cardsPrev) {           
	        //Do your stuff here
			result+=s;
			result+="_";
					
	    }
		for (String s: cardsCurrent) {           
	        //Do your stuff here
			result+=s;
			result+="_";
					
	    }
		result = result.substring(0, result.length()-1);
		return result;
	}
	private String[] getWarCards(int playerID)
	{
		 
		String prevPlayerCardId_1 = this.gameManager.getPlayerByIndex(playerID).getPlayerCards().getFirst();
		this.gameManager.getPlayerByIndex(playerID).getPlayerCards().removeFirst();
		String prevPlayerCardId_2 = this.gameManager.getPlayerByIndex(playerID).getPlayerCards().getFirst();
		this.gameManager.getPlayerByIndex(playerID).getPlayerCards().removeFirst();
		String prevPlayerCardId_3 = this.gameManager.getPlayerByIndex(playerID).getPlayerCards().getFirst();
		this.gameManager.getPlayerByIndex(playerID).getPlayerCards().removeFirst();
		//the fourth card is to play the war
		String prevPlayerCardId_4 = this.gameManager.getPlayerByIndex(playerID).getPlayerCards().getFirst();
		this.gameManager.getPlayerByIndex(playerID).getPlayerCards().removeFirst();
		
		return new String[]{prevPlayerCardId_1, prevPlayerCardId_2,prevPlayerCardId_3,prevPlayerCardId_4};
	}
	private int getPreviousePlayerIndex(int _currentPlayerID)
	{
		//find out who is the previous player
		int playerInx = this.gameManager.getPlayerIndexByKey(_currentPlayerID);
		if(playerInx == 0)
		{
			int playerSize = this.gameManager.getPlayers().size();
			playerInx = playerSize-1;
		}
		else
		{
			--playerInx;
		}
		return playerInx;
	}
	private Player setPlayerNewAttributes(String _userName,Channel channel,int nextEvent)
	{
		Player newPlayer = new Player(channel);
		newPlayer.setUserName(_userName);
		int id = GenerateUniqueId(); 
		int count = getPlayerRegistretionCounter();
		newPlayer.setRegistertionNum(count);
		newPlayer.setId(id);
		newPlayer.setEvent(nextEvent);
		setPlayerCards(newPlayer);
		setNewPlayerCardId(newPlayer);
		return newPlayer;
	}
	
	private void setPlayerInPlayersContainer(Player _player)
	{
		this.gameManager.getPlayers().put(_player.getId(), _player);
	}
	
	private void setPlayerCards(Player _player)
	{
		//this is only good for 2 players 
		int len = this.gameManager.getCardsRandomize().length-1;
		if(_player.getId()==0)
		{			
			for(int i=0;i<(len/2);i++)
			{
				_player.getPlayerCards().push(this.gameManager.getCardsRandomizeByIndex(i));
			}
		}
		else if(_player.getId()==1)
		{
			for(int i=len;i>(len/2);i--)
			{
				_player.getPlayerCards().push(this.gameManager.getCardsRandomizeByIndex(i));
			}
		}
			
		
	}
	
	 
	private void setNewPlayerCardId(Player _player)
	{
		String cardId = _player.getPlayerCards().removeFirst();
		_player.setActivecardid(cardId);
	}
	
	private int GenerateUniqueId()
	{
		int id = this.playerIdCounter;
		this.playerIdCounter++;
		return id;
	}
	
	private int getPlayerRegistretionCounter()
	{
		int count = this.playerRegistretionCounter;
		this.playerRegistretionCounter++;
		return count;
	}
	 
}
