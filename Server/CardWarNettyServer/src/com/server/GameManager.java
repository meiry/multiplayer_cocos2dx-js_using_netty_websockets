package com.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

 

public class GameManager {
	private final static Logger LOG = LoggerManager.GetLogger(GameServerMain.class.getName());
	 
	private final String[] cardsNotRandomBase = {"c1","c2","c3","c4","c5","c6","c7","c8","c9","c10","c11","c12","c13","c14","c15","c16","c17","c18","c19","c20","c21","c22","c23","c24","c25","c26","c27","c28","c29","c30","c31","c32","c33","c34","c35","c36","c37","c38","c39","c40","c41","c42","c43","c44","c45","c46","c47","c48","c49","c50","c51","c52","c53"};
	private final String[] cardsRandomize;
	public final HashMap<String, String> cardsMap;

	private volatile Map<Integer, Player> players; 
	private LinkedList<String> cardsPlayDeck;
	private final GameResponseDispatcher gameResponseDispatcher;

	
	public GameManager( )
	{
		
		cardsMap =  new HashMap<String, String>();
		setCardsHash();
		final ResourceBundle configurationBundle = ResourceBundle.getBundle("configuration");
		//hashmap with predictable iteration order
		players = new LinkedHashMap<Integer, Player>();
		cardsPlayDeck = new LinkedList<String>();		 
		this.cardsRandomize = Arrays.copyOf(cardsNotRandomBase , cardsNotRandomBase .length);
		Collections.shuffle(Arrays.asList(cardsRandomize));
		gameResponseDispatcher = new GameResponseDispatcher(this);
	}
	public GameResponseDispatcher getGameResponseDispatcher() {
		return gameResponseDispatcher;
	}

	public HashMap<String, String> getCardsMap() {
		return cardsMap;
	}
	
	public LinkedList<String> getCardsPlayDeck() {
		return cardsPlayDeck;
	}

	public void setCardsPlayDeck(LinkedList<String> cardsPlayDeck) {
		this.cardsPlayDeck = cardsPlayDeck;
	}
	
	public int getPlayerIndexByKey(int playerKey)
	{
		 
		int pos = new ArrayList<Integer>(players.keySet()).indexOf(playerKey);
		return pos;
	}
	public Player getPlayerByIndex(int inx)
	{
		List<Player> l = new ArrayList<Player>(players.values());
		Player p = l.get(inx);
		return p;
	}
	
	
	public Map<Integer, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Integer, Player> players) {
		this.players = players;
	}
	
	public String[] getCardsRandomize() {
		return cardsRandomize;
	}
	public String getCardsRandomizeByIndex(int i) {
		return cardsRandomize[i];
	}
	
	public int getCardValueById(String cardId)
	{
		String card = this.cardsMap.get(cardId);
		String rightPart= card.split("_")[1];
		String number = rightPart.split(".png")[0];
		int cardIntVal = Integer.valueOf(number);
		return cardIntVal;
	}
	
	public int getNextActivePlayer(int currentActivePlayer)
	{
		int currentPlyInx = getPlayerIndexByKey(currentActivePlayer);
		int activePlayer = 0;
		if(currentPlyInx < (players.size()-1))
		{
			++activePlayer;
		}
		
		return activePlayer;
	}
	
	private void setCardsHash()
	{
		cardsMap.put("c1","cardClubsA_20.png");
		cardsMap.put("c2","cardClubsJ_17.png");
		cardsMap.put("c3","cardClubsK_19.png");
		cardsMap.put("c4","cardClubsQ_18.png");
		cardsMap.put("c5","cardDiamonds10_10.png");
		cardsMap.put("c6","cardDiamonds2_2.png");
		cardsMap.put("c7","cardDiamonds3_3.png");
		cardsMap.put("c8","cardDiamonds4_4.png");
		cardsMap.put("c9","cardDiamonds5_5.png");
		cardsMap.put("c10","cardDiamonds6_6.png");
		cardsMap.put("c11","cardDiamonds7_7.png");
		cardsMap.put("c12","cardDiamonds8_8.png");
		cardsMap.put("c13","cardDiamonds9_9.png");
		cardsMap.put("c14","cardDiamondsA_20.png");
		cardsMap.put("c15","cardDiamondsJ_17.png");
		cardsMap.put("c16","cardDiamondsK_19.png");
		cardsMap.put("c17","cardDiamondsQ_18.png");
		cardsMap.put("c18","cardHearts10_10.png");
		cardsMap.put("c19","cardHearts2_2.png");
		cardsMap.put("c20","cardHearts3_3.png");
		cardsMap.put("c21","cardHearts4_4.png");
		cardsMap.put("c22","cardHearts5_5.png");
		cardsMap.put("c23","cardHearts6_6.png");
		cardsMap.put("c24","cardHearts7_7.png");
		cardsMap.put("c25","cardHearts8_8.png");
		cardsMap.put("c26","cardHearts9_9.png");
		cardsMap.put("c27","cardHeartsA_20.png");
		cardsMap.put("c28","cardHeartsJ_17.png");
		cardsMap.put("c29","cardHeartsK_19.png");
		cardsMap.put("c30","cardHeartsQ.png");
		cardsMap.put("c31","cardJoker_21.png");
		cardsMap.put("c32","cardSpades10_10.png");
		cardsMap.put("c33","cardSpades2_2.png");
		cardsMap.put("c34","cardSpades3_3.png");
		cardsMap.put("c35","cardSpades4_4.png");
		cardsMap.put("c36","cardSpades5_5.png");
		cardsMap.put("c37","cardSpades6_6.png");
		cardsMap.put("c38","cardSpades7_7.png");
		cardsMap.put("c39","cardSpades8_8.png");
		cardsMap.put("c40","cardSpades9_9.png");
		cardsMap.put("c41","cardSpadesA_20.png");
		cardsMap.put("c42","cardSpadesJ_17.png");
		cardsMap.put("c43","cardSpadesK_19.png");
		cardsMap.put("c44","cardSpadesQ_18.png");
		cardsMap.put("c45","cardClubs10_10.png");
		cardsMap.put("c46","cardClubs2_2.png");
		cardsMap.put("c47","cardClubs3_3.png");
		cardsMap.put("c48","cardClubs4_4.png");
		cardsMap.put("c49","cardClubs5_5.png");
		cardsMap.put("c50","cardClubs6_6.png");
		cardsMap.put("c51","cardClubs7_7.png");
		cardsMap.put("c52","cardClubs8_8.png");
		cardsMap.put("c53","cardClubs9_9.png");
	}
 
	
}
