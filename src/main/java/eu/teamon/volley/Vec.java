package eu.teamon.volley;

/**
 * @author teamon
 *
 */

public class Vec<T> {
	public T x;
	public T y;
	
//	public Vec(){
//		this(0, 0);
//	}
	
//	public Vec(String x, String y){
//		this(Float.parseFloat(x), Float.parseFloat(y));
//	}
	
	public Vec(T x, T y){
		this.x = x;
		this.y = y;
	}
}