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
	private int[] score;
	private int index = 0;
    
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
    	resetScore();
    	setStartPosition();
    }
    

    public void setNick(String nick){
    	this.nick = nick;
    }
    
    public String getNick(){
        return this.nick;
    }
    
    public void score(int set){
    	this.score[set]++;
    }
    
    public void setScore(int set, int score){
    	this.score[set] = score;
    }
    
    public int[] getScore(){
    	return this.score;
    }
    
    public void resetScore(){
    	this.score = new int[5];
    }
    
    public void setIndex(int index){
    	this.index = index;
    }
    
    public int getIndex(){
    	return this.index;
    }
   
    public boolean isReady(){ 
    	return ready; 
    }
    
    public void setReady(boolean ready) { 
    	this.ready = ready; 
    }
    
    public boolean hasBall(){ return this.hasBall; }
    
    public void setHasBall(boolean hasBall){
    	this.hasBall = hasBall;
    }
	
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
	
	public void setStartPosition(){
		setPosition(new Vec(side * 0.75f, 0f));
	}
	
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
