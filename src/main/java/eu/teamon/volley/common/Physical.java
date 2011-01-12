package eu.teamon.volley.common;

/**
 * Base physics class.
 * 
 * Holds position and velocity vectors
 */
public class Physical {
	protected Vec pos = new Vec(0f, 0f);
	protected Vec vel = new Vec(0f, 0f);
	
	/**
	 * Sets position vector
	 * 
	 * @param pos position
	 */
    public void setPosition(Vec pos){ 
    	this.pos = pos; 
    }

    /**
     * Returns position vector
     */
    public Vec getPosition(){ 
    	return pos; 
    }
    
    /**
     * Sets velocity vector
     * @param vel velocity
     */
	public void setVelocity(Vec vel){
		this.vel = vel;
	}
	
	/**
	 * Returns velocity vector
	 */
	public Vec getVelocity(){
		return this.vel;
	}
}