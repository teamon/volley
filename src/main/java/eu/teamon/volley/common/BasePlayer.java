package eu.teamon.volley.common;

/**
 * Common player representation
 */
public abstract class BasePlayer extends Physical {
    /**
     * Player's nick
     */
	protected String nick;
    
	/**
     * Player's score array
     */
	protected int[] score;
	
    /**
     * Player's index
     */
	protected int index = 0;
	
    /**
     * Player's side (used for game processing)
     */
	protected int side;
    
    /**
     * Create player setting it's nick
     * 
     * @param nick Nick
     */
    public BasePlayer(String nick){
    	this.nick = nick;
    	resetScore();
    }

    /**
     * Returns player's nick
     * 
     * @see #setNick(String)
     */
    public String getNick(){
        return this.nick;
    }
    
    /**
     * Sets player's nick
     * 
     * @param nick Nick
     * @see #getNick()
     */
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    /**
     * Returns player's index
     * 
     * Used for displaying score
     * 
     * @see #setIndex(int)
     */
    public int getIndex(){
    	return this.index;
    }
    
    /**
     * Sets player's index
     * 
     * @param index - index
     * @see #getIndex()
     */
    public void setIndex(int index){
    	this.index = index;
    }
    
    /**
     * Returns player's side.
     * 
     * Used for game processing
     * 
     * @see #setSide(int)
     */
	public int getSide() { 
		return side; 
	}
    /**
     * Sets player's side
     * 
     * @param side - side (-1 or 1)
     * @see #getSide()
     */
	public void setSide(int side) { 
		this.side = side; 
	}
	
    /**
     * Increments player's score for specified set
     * 
     * @param set - set number
     * @see #getScore()
     * @see #getScore(int)
     * @see #setScore(int, int)
     * @see #resetScore()
     */
    public void score(int set){
    	this.score[set]++;
    }
    
    /**
     * Sets player's score for specified set
     * 
     * @param score - score value
     * @param set - set number
     * @see #score(int)
     * @see #getScore()
     * @see #getScore(int)
     * @see #resetScore()
     */
    public void setScore(int set, int score){
    	this.score[set] = score;
    }
    
    /**
     * Returns player's score array
     *
     * @see #score(int)
     * @see #getScore(int)
     * @see #setScore(int, int)
     * @see #resetScore()
     */
    public int[] getScore(){
    	return this.score;
    }
    
    /**
     * Returns player's score array for sets from 0 to set
     *
     * @param set set number
     * @see #score(int)
     * @see #getScore()
     * @see #setScore(int, int)
     * @see #resetScore()
     */
    public int[] getScore(int set){
    	int[] res = new int[set];
    	
    	for(int i=0; i<set; i++){
    		res[i] = this.score[i];
    	}
    	return res;
    }
    
    /**
     * Clears player's score
     *
     * @see #score(int)
     * @see #getScore(int)
     * @see #setScore(int, int)
     * @see #resetScore()
     */
    public void resetScore(){
    	this.score = new int[5];
    }
}
