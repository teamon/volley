package eu.teamon.volley.common;

public abstract class BasePlayer  extends Physical {
	protected String nick;
	protected int[] score;
	protected int index = 0;
	protected int side;
    
    public BasePlayer(String nick){
    	this.nick = nick;
    	resetScore();
    }

    public String getNick(){
        return this.nick;
    }
    
    public void setNick(String nick){
    	this.nick = nick;
    }
    
    
    public int getIndex(){
    	return this.index;
    }
    
    public void setIndex(int index){
    	this.index = index;
    }
    
    
    
	public int getSide() { 
		return side; 
	}
	
	public void setSide(int side) { 
		this.side = side; 
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
}
