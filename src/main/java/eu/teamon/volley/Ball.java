package eu.teamon.volley;

public class Ball extends Physical{
    public static final float SIZE = 0.05f;

    public Ball(){
		this(new FloatVec(0f,0f));
	}
	
	public Ball(Vec<Float> pos){
		setPosition(pos);
		setVelocity(new FloatVec(0f, 0f));
	}

//
//	public void move(){
//		if(pos )
//		
//		vel.y -= Game.GRAVITY*Game.TIME;
//		pos.y += vel.y*Game.TIME;
//		
//		
//	}


}
