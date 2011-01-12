package eu.teamon.volley.common;

/**
 * Interface for communication with ConnectionThread
 * 
 * @see ConnectionThread
 */
public interface MessageListener {
    public void processMessage(ConnectionThread connection, String message);
    
    public void remove(ConnectionThread connection);
}
