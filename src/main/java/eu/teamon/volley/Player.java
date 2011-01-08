package eu.teamon.volley;

public class Player extends Physical {
	public static final float X_SPEED = 0.01f;
    public static final float JUMP_SPEED = 0.07f;
    public static final float X_MAX = 1f;
    public static final float Y_MAX = 1f;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final float WIDTH = 0.3f;
    public static final float HEIGHT = 0.3f;
	
	private String nick;
	private boolean ready = false;
    
    // Game stuff
	

    private int side;
    
	private boolean movingLeft = false;
	private boolean movingRight = false;    
	private boolean jumping = false;
	
	private boolean hasBall = false;
    
	public Player(){
    	this("");
    }
    
    public Player(String nick){
        this.nick = nick;
        this.side = 0;
    }
    
    public Player(String nick, int side){
        this(side);
        this.nick = nick;
    }
    
    public Player(int side){
        this.nick = "";
        this.side = side;
        if(side == LEFT) this.hasBall = true;
        else this.hasBall = false;
        pos.x = side * 0.75f;
        Logger.debug("Player created, side = " + side);
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }
    

    
    public boolean isReady(){ 
    	return ready; 
    }
    
    public void setReady(boolean ready) { 
    	this.ready = ready; 
    }
    
    public boolean hasBall(){ return this.hasBall; }
	
	public int getSide() { 
		return side; 
	}
	
	public void setSide(int side) { 
		this.side = side; 
	}

	public void setMovingLeft(boolean movingLeft) {	this.movingLeft = movingLeft; }

	public boolean isMovingLeft() {	return this.movingLeft; }
	
	
	public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
		
	public boolean isMovingRight() { return this.movingRight; }
	
	
	public void setJumping(boolean jumping) { 
		if(!this.jumping && jumping && pos.y == 0){
			vel.y = JUMP_SPEED;
		}
		this.jumping = jumping; 
	}
	
	public boolean isJumping(){ return this.jumping; }
	
	public boolean isMoving(){
		return (movingLeft || movingRight);
	}

}
