package eu.teamon.volley;

import java.io.*;
import java.net.*;

/**
 * Low level class for handling socket connections
 * 
 * @author teamon
 */
public class ConnectionThread extends Thread {
    private MessageListener listener;
    private Socket socket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean keep = true;
    
    public ConnectionThread(MessageListener listener, Socket socket){
        super("ConnectionThread");
        this.listener = listener;
        this.socket = socket;
    }
    
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true); // with auto-flush
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
            String inputLine;
            while(keep && (inputLine = in.readLine()) != null){
            	Logger.debug("ConnectionThread: " + inputLine);
                listener.processMessage(this, inputLine);
            }
            
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
    }
    
    public void sendMessage(String message){
        while(out == null){
        	Logger.warn("out == null");
        }
        out.println(message);
    }
    
    public void kill() throws IOException {
        keep = false;
        
        out.close();
        in.close();
        socket.close();
        
        this.listener.remove(this);
    }
}