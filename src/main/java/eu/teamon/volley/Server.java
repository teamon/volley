package eu.teamon.volley;

import java.util.*;
import java.io.*;
import java.net.*;


public class Server extends SmartThread implements MessageListener {
    public static final int DEFAULT_PORT = 7777;
    public static final int CONNECTIONS_LIMIT = 2;
    
    //private int port;
    private ServerSocket socket = null;
    private Map<ConnectionThread, Player> connections;
    private Game game;
    
    public Server() throws IOException {
        this(DEFAULT_PORT);
    }
    
    public Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.connections = new HashMap<ConnectionThread, Player>();     
        this.game = new Game(this);
        // this.game.start();
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
            	if(this.connections.size() < CONNECTIONS_LIMIT){
                    ConnectionThread ct = new ConnectionThread(this, socket.accept());
                    this.connections.put(ct, new Player());
                    ct.start();	                    
            	} else {
            		try {
            			Thread.sleep(500);
            		} catch (InterruptedException e){
            			// do nothing
            		}
            	}
		
            } catch (SocketTimeoutException e){
                // do nothing
                Logger.debug("Socket Timeout");
            } catch (IOException e){
                Logger.error(e.getMessage());
            }
        }
        
        try {
            // close all connections
            for(ConnectionThread ct : connections.keySet()) {
                ct.kill();
            }
            
            socket.close();
        } catch (IOException e){
            Logger.error(e.getMessage());
        }
    }
    
    public void processMessage(ConnectionThread from, String message){
//    	Logger.debug("server got: " + message);
    	Command cmd = Command.parse(message);
    	
    	switch(cmd.id){
    		case Command.CLIENT_CHAT_MESSAGE:
    		{
    			sendToAll(Command.chatMessage(this.connections.get(from), cmd.args[0]));
    		}
    		break;
    		
    		case Command.PLAYER_REGISTERED:
    		{
    			Player player = this.connections.get(from);
    			player.setNick(cmd.args[0]);
				sendToAll(Command.newPlayerRegistered(player));	
				
				// send list of all players to new player
				for (Player p : connections.values()) {
		            from.sendMessage(Command.newPlayerRegistered(p));
		        }
    		}
    		break;
    		
    		case Command.MOVING_LEFT:
    		{
    			this.connections.get(from).setMovingLeft(cmd.args[0].equals("1"));
    		}
    		break;
    		
    		case Command.MOVING_RIGHT:
    		{
    			this.connections.get(from).setMovingRight(cmd.args[0].equals("1"));
    		}
    		break;
    		
    		case Command.JUMPING:
    		{
    			this.connections.get(from).setJumping(cmd.args[0].equals("1"));
    		}
    		break;
    		
    		case Command.SERVE:
    		{
    			if(game.isWaiting()){
    				game.serve(this.connections.get(from));
    			}
    		}
    		break;
    		
    		case Command.PLAYER_READY:
    		{
    			this.connections.get(from).setReady(true);
    			tryStartGame();
    		}
    		break;
    		
    		case Command.DISCONNECT:
    		{
    			game.stop();
    			Player player = this.connections.get(from);
    			sendToAll(Command.playerDisconnected(player));
    			try { 
    				from.kill(); 
    			} catch (IOException e) { 
    				Logger.error(e.getMessage()); 
    			}
    			this.connections.remove(from);
                Logger.debug("Client disconnected. Clients left: " + this.connections.size());
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
    
    public void sendToAll(Command command){
    	String message = command.toString();
    	for (ConnectionThread ct : connections.keySet()) {
            ct.sendMessage(message);
        }
    }
    
    public Collection<Player> getPlayers(){
    	return connections.values();
    }
    
    protected void tryStartGame(){
    	if(ready()){
    		game.start();
			sendToAll(Command.startGame());
    	}
    }
    
    protected boolean ready(){
    	return (this.connections.size() == CONNECTIONS_LIMIT && allPlayersReady());
    }
    
    protected boolean allPlayersReady(){
    	for(Player player : connections.values()){
    		if(!player.isReady()) return false;
    	}
    	return true;
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }
    
}
