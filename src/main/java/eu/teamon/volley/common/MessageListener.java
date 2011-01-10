package eu.teamon.volley.common;


public interface MessageListener {
    public void processMessage(ConnectionThread connection ,String message);
    
    public void remove(ConnectionThread connection);
}
