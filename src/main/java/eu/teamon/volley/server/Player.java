package eu.teamon.volley.server;

import eu.teamon.volley.common.BasePlayer;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Vec;

/**
 * Server side player representation
 */
public class Player extends BasePlayer {
	// game flags
	private boolean movingLeft = false;
	private boolean movingRight = false;    
	private boolean jumping = false;
	private boolean hasBall = false;
	private boolean ready = false;

	/**
	 * Creates new player with empty nick
	 */
	public Player(){
		this("");
	}
	
	/**
	 * Creates player with nick and setups at start position
	 */
    public Player(String nick){
    	super(nick);
    	setStartPosition();
    }
    
    /**
     * Sets ready parameter
     */
    public void setReady(boolean ready) { 
    	this.ready = ready; 
    }
    
    /**
     * Returns true if player is ready
     */
    public boolean isReady(){ 
    	return ready; 
    }
    
    /**
     * Sets whether player hold the ball
     */
    public void setHasBall(boolean hasBall){
    	this.hasBall = hasBall;
    }
    
    /**
     * Returns true if player is currently holding the ball
     * @return
     */
    public boolean hasBall(){ 
    	return this.hasBall; 
    }
    
    /**
     * Sets "moving left" parameter
     */
	public void setMovingLeft(boolean movingLeft) {	
		this.movingLeft = movingLeft; 
	}

    /**
     * Returns true if player is currently moving left
     */
	public boolean isMovingLeft() {	
		return this.movingLeft; 
	}

    /**
     * Sets "moving right" parameter
     */
	public void setMovingRight(boolean movingRight) { 
		this.movingRight = movingRight; 
	}

    /**
     * Returns true if player is currently moving right
     */
	public boolean isMovingRight() { 
		return this.movingRight; 
	}

    /**
     * Returns true if player is currently movin either right of left
     */
	public boolean isMoving(){
		return (movingLeft || movingRight);
	}
	
    /**
     * Sets "jumping" parameter
     */
	public void setJumping(boolean jumping) { 
		if(!this.jumping && jumping && pos.y >= 100){
			vel.y = -Config.PLAYER_JUMP_SPEED/Config.TIME;
		}
		this.jumping = jumping; 
	}

    /**
     * Returns true if player is currently jumping
     */
	public boolean isJumping(){ 
		return this.jumping; 
	}

    /**
     * Setup player at start position
     */
	public void setStartPosition(){
		setPosition(new Vec(side * 25 + 50, 100));
	}

}

