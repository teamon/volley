package eu.teamon.volley;

import java.util.*;
import java.io.*;
import java.net.*;


public class Server extends Thread implements MessageListener {
    public static final int DEFAULT_PORT = 7777;
    
    //private int port;
    private ServerSocket socket = null;
    private Map<ConnectionThread, Player> connections;
    private boolean keep = true;
    private Game game;
    
    public Server() throws IOException {
        this(DEFAULT_PORT);
    }
    
    public Server(int port) throws IOException {
        super("Server");
        
        this.socket = new ServerSocket(port);
        this.connections = new HashMap<ConnectionThread, Player>();     
        this.game = new Game(this);
        this.game.start();
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
                this.connections.put(ct, new Player());
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
            for (Map.Entry<ConnectionThread, Player> entry : connections.entrySet()) {
                entry.getKey().kill();
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
            case 'c': // chat "c [CONTENT]"
            	if(chunks.length == 2){
            		sendToAll(Command.chatMessage(this.connections.get(from), chunks[1]));
            	}
                break;

			case 'r': // register new user "r [NICK]"
				Logger.debug("User registered: " + chunks[1]);
				if(chunks.length == 2){
					this.connections.get(from).setNick(chunks[1]);
					sendToAll(message);	
					
					// send list of all players to new player
					for (Player player : connections.values()) {
			            from.sendMessage(Command.newPlayerRegistered(player));
			        }
					
				}
				break;
				
			case 'm': // player moving "m [l|r] [0|1]"
				if(chunks.length == 2){
					String[] dirAndState = chunks[1].split(" ", 2);
					Player player = this.connections.get(from);
					//Logger.debug("dirAndState[0] = " + dirAndState[0]);
					//Logger.debug("dirAndState[1] = " + dirAndState[1]);
					if(dirAndState[0].equals("r")){
						player.setMovingRight(dirAndState[1].equals("1"));
					} else if(dirAndState[0].equals("l")){
						player.setMovingLeft(dirAndState[1].equals("1"));
					}
				}
				
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
    	for (Map.Entry<ConnectionThread, Player> entry : connections.entrySet()) {
            Logger.debug("Sending: " + message);
            entry.getKey().sendMessage(message);
        }
    }
    
    public Collection<Player> getPlayers(){
    	return connections.values();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }
    
}
