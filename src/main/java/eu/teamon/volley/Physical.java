package eu.teamon.volley;

public class Physical {
	protected Vec pos = new Vec(0f, 0f);
	protected Vec vel = new Vec(0f, 0f);
	
    public void setPosition(Vec pos){ 
    	this.pos = pos; 
    }

    public Vec getPosition(){ 
    	return pos; 
    }
    
	public void setVelocity(Vec vel){
		this.vel = vel;
	}
	
	public Vec getVelocity(){
		return this.vel;
	}
}