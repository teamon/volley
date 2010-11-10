package eu.teamon.volley;

public class Player {
    private String nick;
    
    public Player(String nick){
        this.nick = nick;
    }
    
    public String getNick(){
        Logger.debug("getNick " + this.nick);
        return this.nick;
    }
}