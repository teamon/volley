package eu.teamon.volley;

public class Physical {
	protected Vec<Float> pos = new FloatVec(0f, 0f);
	protected Vec<Float> vel = new FloatVec(0f, 0f);
	
    public void setPosition(Vec<Float> pos){ 
    	this.pos = pos; 
    }

    public Vec<Float> getPosition(){ 
    	return pos; 
    }
    
	public void setVelocity(Vec<Float> vel){
		this.vel = vel;
	}
	
	public Vec<Float> getVelocity(){
		return this.vel;
	}
}