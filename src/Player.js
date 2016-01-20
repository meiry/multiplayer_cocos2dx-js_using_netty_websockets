var TEXT_INPUT_FONT_NAME = "Thonburi";
var TEXT_INPUT_FONT_SIZE = 36;
var TEXT_INPUT_FONT_SIZE_PLAYER = 20;

 var Player = cc.Sprite.extend({
     
    id:null,
    username:null,
    event:null,
    activecardid:null,
    activeplayerid:null,
    registertionnum:null,
    winner:null,
    winnercards:"",
    numcardsleft:null,
    spriteSize:null,
    textFieldUserName:null,
    ctor: function(_id,_username,_activecardid){        
        this.id = _id;
        this.username = _username;
        this.activecardid = _activecardid;
        var cardName = this.getPlayerCardById(this.activecardid);
        this._super("#"+cardName);
       
    },    
    onEnter:function () {        
         
        this._super();  
        this.spriteSize = this.getContentSize();
        this.setPlayerNameCaption(this.username);
        this.setPlayerNumberOfCardsCaption(this.numcardsleft);
       
    },
    onExit:function () {        
         this._super();      
    },
    getPlayerCardById:function(_cardId)
    {
        var cardName =  cards[_cardId];
        return cardName;
    },
    setPlayerNameCaption:function(_name)
    {
        this.textFieldUserName = new cc.TextFieldTTF(_name,
            TEXT_INPUT_FONT_NAME,
            TEXT_INPUT_FONT_SIZE_PLAYER);
        this.textFieldUserName.setTextColor(cc.color.RED);
        this.textFieldUserName.x = this.spriteSize.width / 2;
        this.textFieldUserName.y = 0-TEXT_INPUT_FONT_SIZE_PLAYER;
        this.addChild(this.textFieldUserName,2); 
    },
    updatePlayerNumberOfCardsCaption:function(_numberOfCards)
    {
        this.numcardsleft = _numberOfCards
        this.textFieldNumberOfCards.setString("Cards:"+this.numcardsleft); 
    },
    setPlayerNumberOfCardsCaption:function(_numberOfCards)
    {
        this.textFieldNumberOfCards = new cc.TextFieldTTF("Cards:"+_numberOfCards,
            TEXT_INPUT_FONT_NAME,
            TEXT_INPUT_FONT_SIZE_PLAYER);
        this.textFieldNumberOfCards.setTextColor(cc.color.RED);
        this.textFieldNumberOfCards.x = this.spriteSize.width / 2;
        this.textFieldNumberOfCards.y = 0-(TEXT_INPUT_FONT_SIZE_PLAYER+TEXT_INPUT_FONT_SIZE_PLAYER);
        this.addChild(this.textFieldNumberOfCards,2); 
    },
    setNewCardById:function (_cardid)
    {
        //get the right card from the cards hash
        var cardName =  cards[_cardid];
        this.activecardid = cardName;
        //this._super(this.playerSpriteFrameName);
        this.setSpriteFrame(cardName);
    },
    
      
});
 


