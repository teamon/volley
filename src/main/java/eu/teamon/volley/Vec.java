package eu.teamon.volley;

/**
 * @author teamon
 *
 */

public abstract class Vec<T extends Number> {
	public T x;
	public T y;
	
	public Vec(T x, T y){
		this.x = x;
		this.y = y;
	}
	
	public Vec(double angle, double length){
		this.x = Math.cos(angle)*length;
		this.y = Math.sin(angle)*length;
	}

	abstract public Vec<T> add(Vec<T> that);
	abstract public Vec<T> subtract(Vec<T> that);
	abstract public Vec<T> negate();
	abstract public Vec<T> negateX();
	abstract public Vec<T> negateY();
	abstract public T distanceTo(Vec<T> that);
	
	public double getLength(){
		return Math.sqrt(Math.pow(this.x.doubleValue(), 2) + Math.pow(this.y.doubleValue(), 2));
	}
	
	public double getAngle(){
		return Math.asin(this.y.doubleValue()/getLength());
	}
	
	abstract public Vec<T> withAngle(double angle);
	
	protected double calculateDistanceTo(Vec<T> that){
		return Math.sqrt(Math.pow(this.x.doubleValue() - that.x.doubleValue(), 2) + Math.pow(this.y.doubleValue() - that.y.doubleValue(), 2));
	}
	
	
	
}
