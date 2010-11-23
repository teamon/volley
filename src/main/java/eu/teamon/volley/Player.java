package eu.teamon.volley;

public class Player {
    private final int X_SPEED = 4;
	
	private String nick;
    
    // Game stuff
    private int x;
    private int y;
    
    
    
    public Player(){
    	this("");
    }
    
    public Player(String nick){
        this.nick = nick;
        this.x = 0;
        this.y = 0;
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void incrementX(){
		this.x += X_SPEED;
		// TODO: Add range handling!!
	}
	
	public void decrementX(){
		this.x -= X_SPEED;
		// TODO: Add range handling
	}
    
    
}
