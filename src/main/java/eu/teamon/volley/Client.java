package eu.teamon.volley;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements MessageListener {
    private Chat chat;
    private Player player;
	private Map<String, Player> players;
    private ConnectionThread connection;
    
    public Client(){
    	
    }
    
    public void setChat(Chat chat){
    	this.chat = chat;
    }
    
    public void connect(String host, int port, Player player) throws IOException {
        this.player = player;
		this.players = new HashMap<String, Player>();
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
            case 'c':
                if(chunks.length == 2){
                    String[] nickAndContent = chunks[1].split(" ", 2);
                    if(nickAndContent.length == 2){
                        Logger.debug("Add chat message");
                        chat.addMessage(new Message(new Player(nickAndContent[0]), nickAndContent[1]));
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
    
    public Chat getChat(){
        return this.chat;
    }
    
    public Player getPlayer(){
        return this.player;
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