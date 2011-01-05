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
	
	abstract public Vec<T> add(Vec<T> that);
}
