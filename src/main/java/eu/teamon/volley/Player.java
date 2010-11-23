package eu.teamon.volley;

public class Player {
    private final float X_SPEED = 0.005f;
    private final float X_MAX = 1f;
	
	private String nick;
    
    // Game stuff
    private float x;
    private float y;
    
    
    
    public Player(){
    	this("");
    }
    
    public Player(String nick){
        this.nick = nick;
        this.x = 0f;
        this.y = 0f;
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void incrementX(){
		this.x += X_SPEED;
		if(this.x > X_MAX) this.x = X_MAX;
	}
	
	public void decrementX(){
		this.x -= X_SPEED;
		if(this.x < -X_MAX) this.x = -X_MAX;
	}
    
    
}
