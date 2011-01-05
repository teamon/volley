package eu.teamon.volley;

public class Player {
    private final float X_SPEED = 0.01f;
    private final float X_MAX = 1f;
    public final int LEFT = -1;
    public final int RIGHT = 1;
    public final float TIME = 1;
	
	private String nick;
	private boolean ready = false;
    
    // Game stuff
	
	private Vec<Float> pos = new Vec<Float>(0f, 0f);
	private Vec<Float> vel = new Vec<Float>(0f, 0f);
    private int side;
    
	private boolean movingLeft = false;
	private boolean movingRight = false;    
    
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
        pos.x = side * 0.75f;
        Logger.debug("Player created, side = " + side);
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }
    
    public void setPos(Vec<Float> pos){ 
    	this.pos = pos; 
    }

    public Vec<Float> getPos(){ 
    	return pos; 
    }
    
    public boolean isReady(){ 
    	return ready; 
    }
    
    public void setReady(boolean ready) { 
    	this.ready = ready; 
    }
	
	public int getSide() { 
		return side; 
	}
	
	public void setSide(int side) { 
		this.side = side; 
	}
	
//	public void incrementX(){
//		this.x += X_SPEED;
//		if(this.x > X_MAX) this.x = X_MAX;
//	}
	
//	public void decrementX(){
//		this.x -= X_SPEED;
//		if(this.x < -X_MAX) this.x = -X_MAX;
//	}
	
	public void setMovingLeft(boolean movingLeft) {	this.movingLeft = movingLeft; }
	public void setMovingRight(boolean movingRight) { this.movingRight = movingRight; }
	
	public boolean isMoving(){
		return (movingLeft || movingRight);
	}
	
	public boolean move(){
		if(movingLeft) {
			vel.x = -X_SPEED;
		} else if(movingRight) {
			vel.x = X_SPEED;
		} else {
			vel.x = 0f;
		}
		
		pos.x += vel.x*TIME;
		pos.y += vel.y*TIME;	
		
		return (movingLeft || movingRight);
	}

}
