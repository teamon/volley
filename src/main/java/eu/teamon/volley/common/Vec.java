package eu.teamon.volley.common;

/**
 * 2D vector (point) representation
 * 
 * Holds (x,y) values
 */
public class Vec {
	public float x;
	public float y;
	
	/**
	 * Create new vector with (0,0) value
	 * @param x x-value
	 * @param y y-value
	 */
	public Vec(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns new Vector with specified angle and length
	 * @param angle
	 * @param length
	 */
	public static Vec withAngleAndLength(float angle, float length){
		return new Vec((float)(Math.cos(angle)*length), (float)(Math.sin(angle)*length));
	}

	/**
	 * Returns new Vector v' = this + that
	 */
	public Vec add(Vec that){
		return new Vec(this.x + that.x, this.y + that.y);
	}
	
	/**
	 * Returns new Vector v' = this - that
	 */
	public Vec subtract(Vec that){
		return new Vec(this.x - that.x, this.y - that.y);
	}
	
	/**
	 * Returns distance to other point
	 */
	public float distanceTo(Vec that){
		return (float)Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
	}
	
	/**
	 * Returns new Vector with negate values (x', y') = (-x, -y)
	 */
	public Vec negate(){
		return new Vec(-this.x, -this.y);
	}
	
	/**
	 * Returns new Vector with negated x-value (x', y') = (-x, y)
	 */
	public Vec negateX(){
		return new Vec(-this.x, this.y);
	}
	
	/**
	 * Returns new Vector with negated y-value (x', y') = (x, -y)
	 */
	public Vec negateY(){
		return new Vec(this.x, -this.y);
	}
	
	/**
	 * Returns new Vector scale with factor (x', y') = (factor*x, factor*y)
	 */
	public Vec scale(float factor){
		return new Vec(x*factor, y*factor);
	}
	
	/**
	 * Returns length of vector
	 */
	public float getLength(){
		return (float)Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}
	
	/**
	 * Returns vector angle (angle between vector and OX axis)
	 */
	public float getAngle(){
		if(y >= 0){
			return (float)Math.acos(this.x/getLength());
		} else {
			return 2*(float)Math.PI - (float)Math.acos(this.x/getLength());
		}
	}
	
	/**
	 * Returns new Vector with same length and new angle
	 */
	public Vec withAngle(float angle){
		return Vec.withAngleAndLength(angle, getLength());
	}
	
	/**
	 * Returns vectors' angle difference
	 */
	public float angleTo(Vec that){
		return getAngle() - that.getAngle();
	}
	
	/**
	 * Returns absolute vectors' angle difference (from 0 to PI)
	 */
	public float absoluteAngleTo(Vec that){
		float x = Math.abs(angleTo(that));
		if(x > Math.PI) x = 2*(float)Math.PI - x;
		return x;
	}
	
	/**
	 * String representation "(x, y)"
	 */
	public String toString(){
		return "(" + this.x + "," + this.y + ")";
	}
		
}
