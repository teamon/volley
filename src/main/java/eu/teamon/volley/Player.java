package eu.teamon.volley;

public class Player {
    private static final float X_SPEED = 0.01f;
    private static final float JUMP_SPEED = 0.07f;
    private static final float X_MAX = 1f;
    private static final float Y_MAX = 1f;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
	
	private String nick;
	private boolean ready = false;
    
    // Game stuff
	
	private Vec<Float> pos = new FloatVec(0f, 0f);
	private Vec<Float> vel = new FloatVec(0f, 0f);
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
    
    public boolean hasBall(){ return this.hasBall; }
	
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
	public void setJumping(boolean jumping) { 
		if(!this.jumping && jumping && pos.y == 0){
			vel.y = JUMP_SPEED;
		}
		this.jumping = jumping; 
	}
	
	public boolean isMoving(){
		return (movingLeft || movingRight);
	}
	
	public boolean move(){
		boolean moved = false;
		
		if(movingLeft) {
			vel.x = -X_SPEED;
		} else if(movingRight) {
			vel.x = X_SPEED;
		} else {
			vel.x = 0f;
		}
		
		if(jumping || pos.y > 0){
			vel.y -= Game.GRAVITY*Game.TIME;
			pos.y += vel.y*Game.TIME;
			
			if(pos.y < 0) pos.y = 0f;
			else if(pos.y > Y_MAX) pos.y = Y_MAX;
			
			if(pos.y == 0){ 
				setJumping(false);
				vel.y = 0f;
			}
			
			moved = true;
		}
		
		if(vel.x != 0){
			pos.x += vel.x*Game.TIME;
			
			if(side*pos.x > X_MAX) pos.x = side*X_MAX;
			else if(side*pos.x < 0) pos.x = 0f;
			moved = true;
		}

		

		return moved;
	}

}
