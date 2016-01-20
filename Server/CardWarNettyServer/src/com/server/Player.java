package com.server;

import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

import com.json.JSONObject;

import io.netty.channel.Channel;

public class Player {
	private final static Logger LOG = LoggerManager.GetLogger(GameServerMain.class.getName());
	private LinkedList<String> PlayerCards;
	private String userName;
	private int Event;
	//Session channel
	private Channel channel;
	//Player Json massage
	private String playerJson;
	//the player which is active and has the turn
	private int activeplayerid;
	private int id;
	private String activecardid;
	private int registertionNum;
	private int winner;
	private String winnercards;
	private String deckcard;
	//mark the end game the value will be the winner id
	private int endgame;
	
	 
	
	public Player()
	{
		this.channel = null;
		Init();
	}
	
	public Player(Channel _channel)
	{
		this.channel = _channel;
		Init(); 
	}
	
	public String getWinnercards() {
		return winnercards;
	}

	public void setWinnercards(String winnercards) {
		this.winnercards = winnercards;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}
	
	public int getEndgame() {
		return endgame;
	}

	public void setEndgame(int endgame) {
		this.endgame = endgame;
	}

	private void Init()
	{
		this.setPlayerCards(new LinkedList<String>());
		this.winner =-1;
		this.winnercards="";
		this.deckcard="";
		this.endgame =-1;
	}
	
    public String getPlayerJson() {
		return playerJson;
	}

	public void setPlayerJson(String playerJson) {
		this.playerJson="";
		this.playerJson = playerJson;
	}
	public String getDeckcard() {
		return deckcard;
	}

	public void setDeckcard(String deckcard) {
		this.deckcard = deckcard;
	}

	public int getActiveplayerid() {
		return activeplayerid;
	}

	public void setActiveplayerid(int activeplayerid) {
		this.activeplayerid = activeplayerid;
	}

	 
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Channel getChannel() {
		return channel;
	}
	 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LinkedList<String> getPlayerCards() {
		return PlayerCards;
	}

	public void setPlayerCards(LinkedList<String> playerCards) {
		PlayerCards = playerCards;
	}

	public int getEvent() {
		return Event;
	}

	public void setEvent(int event) {
		Event = event;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getActivecardid() {
		return activecardid;
	}

	public void setActivecardid(String activecardid) {
		this.activecardid = activecardid;
	}
	public int getRegistertionNum() {
		return registertionNum;
	}

	public void setRegistertionNum(int registertionNum) {
		this.registertionNum = registertionNum;
	}

	
}
