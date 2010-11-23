package eu.teamon.volley;

public abstract class SmartThread extends Thread {
	protected boolean keep = true;
	public void kill(){
		keep = false;
	}
}
