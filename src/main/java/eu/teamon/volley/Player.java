package eu.teamon.volley;

public class Player {
    private String nick;
    
    public Player(){
    	this("");
    }
    
    public Player(String nick){
        this.nick = nick;
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        Logger.debug("getNick " + this.nick);
        return this.nick;
    }
}