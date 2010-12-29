package eu.teamon.volley;

public class Player {
    private final float X_SPEED = 0.01f;
    private final float X_MAX = 1f;
	
	private String nick;
	private boolean ready = false;
    
    // Game stuff
    private float x;
    private float y;
    
	private boolean movingLeft = false;
	private boolean movingRight = false;    
    
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
    
    public boolean isReady(){ return ready; }
    public void setReady(boolean ready) { this.ready = ready; }

	public float getX() { return x; }
	public void setX(float x) {	this.x = x; }

	public float getY() { return y; }
	public void setY(float y) { this.y = y; }
	
	public void incrementX(){
		this.x += X_SPEED;
		if(this.x > X_MAX) this.x = X_MAX;
	}
	
	public void decrementX(){
		this.x -= X_SPEED;
		if(this.x < -X_MAX) this.x = -X_MAX;
	}
	
	public void setMovingLeft(boolean movingLeft) {	this.movingLeft = movingLeft; }
	public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
	
	public boolean isMoving(){
//		Logger.debug("player#isMoving: " + movingLeft + " " + movingRight);
		return (movingLeft || movingRight);
	}
	
	public void move(){
//		Logger.debug("Move player: " + movingLeft + " " + movingRight);
		if(movingLeft) decrementX();
		if(movingRight) incrementX();
	}
    
    
}
