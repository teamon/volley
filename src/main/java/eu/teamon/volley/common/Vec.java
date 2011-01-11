package eu.teamon.volley.common;

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
		return (float)Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
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
	
	public Vec scale(float factor){
		return new Vec(x*factor, y*factor);
	}
	
	public float getLength(){
		return (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	public float getAngle(){
		if(y >= 0){
			return (float)Math.acos(this.x/getLength());
		} else {
			return 2*(float)Math.PI - (float)Math.acos(this.x/getLength());
		}
	}
	
	public Vec withAngle(float angle){
		return Vec.withAngleAndLength(angle, getLength());
	}
	
	public float angleTo(Vec that){
		return getAngle() - that.getAngle();
	}
	
	public float absoluteAngleTo(Vec that){
		float x = Math.abs(angleTo(that));
		if(x > Math.PI) x = 2*(float)Math.PI - x;
		return x;	}
	
	public String toString(){
		return "(" + this.x + "," + this.y + ")";
	}
		
}
