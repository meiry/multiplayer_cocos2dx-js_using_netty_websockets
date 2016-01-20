var TEXT_INPUT_FONT_NAME = "Thonburi";
var TEXT_INPUT_FONT_SIZE = 36;
var sceneIdx = -1;
var TEXT_FIELD_ERR = 1;
 
//--------------------------------------------------------------\
 
var LoginLayer = cc.Layer.extend({
    sprite:null,
    size:null,
    textFieldUserName:null,
    textErorrConnectedField:null, 
    enterWorldScene:null,
    textField:null,
    ctor:function () {
        //////////////////////////////
        // 1. super init first
        this._super();
        this.size = cc.winSize;        
        return true;
    },
     onEnter:function () {
        //----start2----onEnter
        this._super();         
        var winSize = cc.director.getWinSize();
        
        
        // Create the textfield
        this.textField = new ccui.TextField();
        this.textField.setMaxLengthEnabled(true);
        this.textField.setMaxLength(30);
        this.textField.setTouchEnabled(true);
        this.textField.fontName = TEXT_INPUT_FONT_NAME;
        this.textField.fontSize = 30;
        this.textField.placeHolder = "[click here for user name]";
        this.textField.x = winSize.width / 2.0;
        this.textField.y = winSize.height / 2.0;
        this.textField.addEventListener(this.textFieldEvent, this);
        this.addChild(this.textField);
        
        
        cc.MenuItemFont.setFontSize(35);
        var menu = new cc.Menu(
            new cc.MenuItemFont("Login Game", this.loginGame, this)
            );
        menu.alignItemsVerticallyWithPadding(4);
        menu.x = this.size.width / 2;
        menu.y = this.size.height / 2 - 50;
        this.addChild(menu,5);
    },
     onExit:function () {
         this._super();    
    },
    createErrorMsg:function () {
            this.textErorrConnectedField = new cc.TextFieldTTF("Error Connecting Server try again",
                                                            TEXT_INPUT_FONT_NAME,
                                                            TEXT_INPUT_FONT_SIZE+20);
            this.textErorrConnectedField.setTag(TEXT_FIELD_ERR);            
            this.textErorrConnectedField.x = cc.winSize.width / 2;
            this.textErorrConnectedField.y = cc.winSize.height / 2 +50;
            this.addChild(this.textErorrConnectedField,2);  
    },
    
    loginGame:function (sender) {
        //remove error msg if any 
        if(this.getChildByTag(TEXT_FIELD_ERR)!==null)
        {
            this.removeChildByTag(TEXT_FIELD_ERR);
        }
        
        //check login in the server 
        var txtUserName = this.textField.getString();
        var config = {
                    event:Events.LOGIN,
                    username:txtUserName
	    };
        var message = Encode(config);
        try {
            ws = new WebSocket("ws://localhost:8888/ws"); 
            ws.onopen = function() {
                                    
                    ws.send(message);
                
            };
            ws.onmessage = function (e) {
                console.log("app->srv.ws.onmessage():"+e.data);
                if(e.data!==null || e.data !== 'undefined')
                { 
                      var jsonFromClient = Decode(e.data);
                      if(jsonFromClient.event === Events.LOGIN_DONE)
                      {
                           enterWorldScene = new EnterWorldScene(jsonFromClient);
                           cc.director.runScene(enterWorldScene);
                      }
                }
            };
            ws.onclose = function (e) {
                    
            };
            ws.onerror = function (e) {
                  
            };
        } catch (e) {
            console.error('Sorry, the web socket at "%s" is un-available', url);
        }
         
    },
    
    onClickTrackNode:function (clicked) {
        var textField = this._trackNode;
        if (clicked) {
            // TextFieldTTFTest be clicked
            //cc.log("TextFieldTTFDefaultTest:CCTextFieldTTF attachWithIME");
            textField.attachWithIME();
        } else {
            // TextFieldTTFTest not be clicked
            //cc.log("TextFieldTTFDefaultTest:CCTextFieldTTF detachWithIME");
            textField.detachWithIME();
        }
    },
});

var LoginScene = cc.Scene.extend({
    onEnter:function () {
        this._super();
        var layer = new LoginLayer();
        this.addChild(layer);
    }
    
});

