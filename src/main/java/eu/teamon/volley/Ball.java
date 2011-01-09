package eu.teamon.volley;

public class Ball extends Physical{
    public static final float SIZE = 0.2f;

    public Ball(){
		this(new Vec(0f,0f));
	}
	
	public Ball(Vec pos){
		setPosition(pos);
		setVelocity(new Vec(0f, 0f));
	}
}
