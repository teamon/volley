package eu.teamon.volley.common;


public class Ball extends Physical{
    public Ball(){
		this(new Vec(0f,0f));
	}
	
	public Ball(Vec pos){
		setPosition(pos);
		setVelocity(new Vec(0f, 0f));
	}
	
	@Override
	public void setVelocity(Vec vel){
		// abs(x) <= MAX		
		super.setVelocity(new Vec(Math.signum(vel.x)*Math.min(Math.abs(vel.x), Config.BALL_MAX_SPEED), Math.signum(vel.y)*Math.min(Math.abs(vel.y), Config.BALL_MAX_SPEED)));
	}
	
	
}
