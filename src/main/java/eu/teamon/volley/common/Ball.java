package eu.teamon.volley.common;


public class Ball extends Physical{
    public Ball(){
		this(new Vec(0f,0f));
	}
	
	public Ball(Vec pos){
		setPosition(pos);
		setVelocity(new Vec(0f, 0f));
	}
}
