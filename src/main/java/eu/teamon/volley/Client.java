package eu.teamon.volley;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements MessageListener {
    private ClientFrame frame;
    private Player player;
	private Map<String, Player> players;
    private ConnectionThread connection;
    private ClientGame game;
    
    public Client(ClientFrame frame){
    	this.frame = frame;
		this.players = new HashMap<String, Player>();
    }
    
    public void connect(String host, int port, Player player) throws IOException {
        this.player = player;
		this.players.put(player.getNick(), player);
        this.connection = new ConnectionThread(this, new Socket(host, port));
        this.connection.start();
		sendMessage(Command.newPlayerRegistered(player));
    }
    
    public void sendMessage(String message){
        Logger.debug("Client#sendMessage(" + message + ")");
        this.connection.sendMessage(message);
    }
    
    public void processMessage(ConnectionThread from, String message){
        Logger.debug("Client#processMessage(" + message + ")");
        
        String[] chunks = message.split(" ", 2);
        switch(chunks[0].charAt(0)){
            case 'c': // c [nick] [content]
                if(chunks.length == 2){
                    String[] nickAndContent = chunks[1].split(" ", 2);
                    if(nickAndContent.length == 2){
                    	Player player = players.get(nickAndContent[0]);
                    	if(player != null){
                    		frame.addChatMessage(player, nickAndContent[1]);
                    	}
                    }   
                }
                break;
                
            case 's': // s [nick] [xpos] [ypos]
            	if(chunks.length == 2){
            		String[] nickAndPosition = chunks[1].split(" ", 3);
            		Player player = this.players.get(nickAndPosition[0]);
            		if(player != null){
            			player.setX(Float.parseFloat(nickAndPosition[1]));
            			player.setY(Float.parseFloat(nickAndPosition[2]));
            		}
            		
            	}
            	break;
            	
            case 'r': // r [nick]
            	// register new game player
            	if(chunks.length == 2){
            		if(!players.containsKey(chunks[1])){
            			players.put(chunks[1], new Player(chunks[1]));
            		}
            	}
            	break;
                
            default:  
                Logger.error("Unknown command: " + message);
                break;
        }
        
        // 
        // String[] chunks = message.split(" ", 2);
        // switch(Integer.parseInt(chunks[0])){
        //     case 'c':
        //         if(chunks.length == 3) chat.addMessage(new Message(new Player(chunks[1]), chunks[2]));
        //         break;
        //     default:
        //         Logger.error("Unknown command: " + message);
        //         break;
        // }
    }
    
    public void remove(ConnectionThread connection){
        Logger.error("Implement me!");
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public Collection<Player> getPlayers(){
    	return players.values();
    }
    
    public void disconnect(){
        try {                
            if(this.connection != null) this.connection.kill();
        } catch (IOException e){
            Logger.warn("Temporary silent fail");
        } finally {
        	this.connection = null;
        }
    }
    
    public boolean isConnected(){
    	return this.connection != null;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }
}