package eu.teamon.volley.common;

import java.io.*;
import java.net.*;


/**
 * Low level class for handling socket connections
 */
public class ConnectionThread extends SmartThread {
    /**
     * Connection message listener
     */
    private MessageListener listener;
    private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    
    /**
     * Create new ConnectionThread
     */
    public ConnectionThread(MessageListener listener, Socket socket){
        this.listener = listener;
        this.socket = socket;
    }
    
    /**
     * Connects to socket
     */
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true); // with auto-flush
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
            String inputLine;
            while(keep && (inputLine = in.readLine()) != null){
                listener.processMessage(this, inputLine);
            }
            
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }
    
    /**
     * Send message through socket
     * 
     * @param message
     */
    public void sendMessage(String message){
        while(out == null){
        	Logger.warn("out == null");
        }
        out.println(message);
    }
    
	/**
	 * Send Command through socket
	 * @param command
	 * @see Command
	 */
    public void sendMessage(Command command){
    	sendMessage(command.toString());
    }
    
    /**
     * Close connection
     */
    @Override
    public void kill() {
        super.kill();
        
        out.close();
        try { in.close(); } catch (IOException e) { /* do nothing */ }
        try { socket.close(); } catch (IOException e) { /* do nothing */ }
        
        listener.remove(this);
    }
}