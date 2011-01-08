package eu.teamon.volley;

/**
 * @author teamon
 *
 */

public class Vec {
	public float x;
	public float y;
	
	public Vec(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public static Vec withAngleAndLength(float angle, float length){
		return new Vec((float)(Math.cos(angle)*length), (float)(Math.sin(angle)*length));
	}

	public Vec add(Vec that){
		return new Vec(this.x + that.x, this.y + that.y);
	}
	
	public Vec subtract(Vec that){
		return new Vec(this.x - that.x, this.y - that.y);
	}
	
	public float distanceTo(Vec that){
		float d = (float)Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
		Logger.debug("(" + this.x + "," + this.y + ") <-> ("+ that.x + "," + that.y + ") => " + d);
		return d;
	}
	
	public Vec negate(){
		return new Vec(-this.x, -this.y);
	}
	
	public Vec negateX(){
		return new Vec(-this.x, this.y);
	}
	
	public Vec negateY(){
		return new Vec(this.x, -this.y);
	}
	
	public float getLength(){
		return (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public float getAngle(){
		return (float)Math.asin(this.y/getLength());
	}
	
	public Vec withAngle(float angle){
		return Vec.withAngleAndLength(angle, getLength());
	}
	
	public String toString(){
		return "(" + this.x + "," + this.y + ")";
	}
		
}
