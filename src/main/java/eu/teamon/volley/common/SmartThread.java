package eu.teamon.volley.common;

/**
 * "Smart" thread.
 * 
 * Uses kill method for safe thread stop
 * 
 * @see #kill()
 */
public abstract class SmartThread extends Thread {
	protected boolean keep = true;
	public void kill(){
		keep = false;
	}
}
