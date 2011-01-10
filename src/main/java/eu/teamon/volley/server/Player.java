package eu.teamon.volley.server;

import eu.teamon.volley.common.BasePlayer;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Vec;

public class Player  extends BasePlayer {
	private boolean movingLeft = false;
	private boolean movingRight = false;    
	private boolean jumping = false;
	private boolean hasBall = false;
	private boolean ready = false;

	
	public Player(){
		this("");
	}
	
    public Player(String nick){
    	super(nick);
    	setStartPosition();
    }
    
    public void setReady(boolean ready) { 
    	this.ready = ready; 
    }
    
    public boolean isReady(){ 
    	return ready; 
    }
    
    public void setHasBall(boolean hasBall){
    	this.hasBall = hasBall;
    }
    
    public boolean hasBall(){ 
    	return this.hasBall; 
    }
    
	public void setMovingLeft(boolean movingLeft) {	
		this.movingLeft = movingLeft; 
	}

	public boolean isMovingLeft() {	
		return this.movingLeft; 
	}
	
	public void setMovingRight(boolean movingRight) { 
		this.movingRight = movingRight; 
	}
		
	public boolean isMovingRight() { 
		return this.movingRight; 
	}
	
	public boolean isMoving(){
		return (movingLeft || movingRight);
	}
	
	
	public void setJumping(boolean jumping) { 
		if(!this.jumping && jumping && pos.y == 0){
			vel.y = Config.PLAYER_JUMP_SPEED;
		}
		this.jumping = jumping; 
	}
	
	public boolean isJumping(){ 
		return this.jumping; 
	}
	
	
	public void setStartPosition(){
		setPosition(new Vec(side * 25 + 50, 100));
	}

}

