package eu.teamon.volley.common;



/**
 * Ball representation
 */
public class Ball extends Physical {
	/**
	 * Default constructor
	 * 
	 * Sets default velocity to (0,0)
	 * 
	 */
    public Ball(){
		this(new Vec(0f,0f));
	}
    
    /**
     * Create ball setting it's position
     * 
     * @param pos position
     */
	public Ball(Vec pos){
		setPosition(pos);
		setVelocity(new Vec(0f, 0f));
	}
	
    /**
     * Sets velocity
     * 
     * @param vel velocity
     * @see Physical#setVelocity(Vec)
     */
	@Override
	public void setVelocity(Vec vel){
		// abs(x) <= MAX		
		super.setVelocity(new Vec(Math.signum(vel.x)*Math.min(Math.abs(vel.x), Config.BALL_MAX_SPEED), Math.signum(vel.y)*Math.min(Math.abs(vel.y), Config.BALL_MAX_SPEED)));
	}
}
