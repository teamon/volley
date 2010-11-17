package eu.teamon.volley;

public interface MessageListener {
    public void processMessage(ConnectionThread connection ,String message);
    
    public void remove(ConnectionThread connection);
}
