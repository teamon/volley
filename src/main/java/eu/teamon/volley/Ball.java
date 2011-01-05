package eu.teamon.volley;

public class Ball {
	private Vec<Float> pos;
	private Vec<Float> vel;

	public Ball(){
		this.pos = new FloatVec(0f,0f);
		this.vel = new FloatVec(0f,0f);
	}
	
	public Ball(Vec<Float> pos){
		this.pos = pos;
		this.vel = new FloatVec(0f, 0f);
	}

	public Vec<Float> getPos(){ return this.pos; }
	
	public void setPos(Vec<Float> pos){
		this.pos = pos;
	}

	public void move(){
		vel.y -= Game.GRAVITY*Game.TIME;
		pos.y += vel.y*Game.TIME;
	}


}
