package eu.teamon.volley;

import java.util.*;
import java.io.*;
import java.net.*;


public class Server extends Thread implements MessageListener {
    public static final int DEFAULT_PORT = 7777;
    
    private int port;
    private ServerSocket socket = null;
    private List<ConnectionThread> connections;
	private Map<String, Player> players;
    private boolean keep = true;
    
    public Server() throws IOException {
        this(DEFAULT_PORT);
    }
    
    public Server(int port) throws IOException {
        super("Server");
        this.port = port;
        this.socket = new ServerSocket(port);
        
        this.connections = new ArrayList<ConnectionThread>();
		this.players = new HashMap<String, Player>();        
        start();
    }
    
    public void run(){
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e){
            Logger.error(e.getMessage());
        }
        
        while(keep){
            try {
                ConnectionThread ct = new ConnectionThread(this, socket.accept());
                this.connections.add(ct);
                Logger.debug("Client connected. Clients num: " + this.connections.size());
                ct.start();				
            } catch (SocketTimeoutException e){
                // do nothing
                Logger.debug("Socket Timeout");
            } catch (IOException e){
                Logger.error(e.getMessage());
            }
        }
        
        try {
            // close all connections
            for(ConnectionThread ct : this.connections){
                ct.kill();
            }
            socket.close();
        } catch (IOException e){
            Logger.error(e.getMessage());
        }
    }
    
    public void kill(){
        Logger.debug("Kill Server");
        keep = false;
    }
    
    public void processMessage(ConnectionThread from, String message){
        Logger.debug("Server#processMessage(" + message + ")");
        String[] chunks = message.split(" ", 2);
    
        switch(chunks[0].charAt(0)){
            case 'c': // chat "c [NICK] [CONTENT]"
                sendToAll(message);
                // if(chunks.length == 3) chat.addMessage(new Message(new Player(chunks[1]), chunks[2]))
                break;

			case 'r': // register new user "r [NICK]"
				sendToAll(message);
				break;

            default:
                Logger.error("Unknown command: " + message);
                break;
        }
    }
    
    public void remove(ConnectionThread connection){
        this.connections.remove(connection);
    }
    
    public void sendToAll(String message){
        for(ConnectionThread ct : this.connections){
            Logger.debug("Sending: " + message);
            ct.sendMessage(message);
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }
    
}
