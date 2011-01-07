package eu.teamon.volley;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client implements MessageListener {
    private ClientFrame frame;
    private Player player;
	private Map<String, Player> players;
    private ConnectionThread connection;
    
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
        
    public void sendMessage(Command command){
    	this.connection.sendMessage(command.toString());
    }
    
    public void processMessage(ConnectionThread from, String message){
    	Command cmd = Command.parse(message);
    	    	
    	switch(cmd.id){
    		case Command.SERVER_CHAT_MESSAGE:
    		{
    			Player player = players.get(cmd.args[0]);
    			if(player != null){
            		frame.addChatMessage(player, cmd.args[1]);
            	}
    		}
    		break;
    		
    		case Command.PLAYER_POSITION:
    		{
        		Player player = this.players.get(cmd.args[0]);
        		if(player != null){
        			player.setPosition(new Vec(Float.parseFloat(cmd.args[1]), Float.parseFloat(cmd.args[2])));
        		}
    		}
    		break;
    		
    		case Command.BALL_POSITION:
    		{
    			this.frame.getGame().setBallPosition(new Vec(Float.parseFloat(cmd.args[0]), Float.parseFloat(cmd.args[1])));
    		}
    		break;
    		
    		case Command.PLAYER_REGISTERED:
    		{
    			String nick = cmd.args[0];
        		if(players.containsKey(nick)){
        			players.get(nick).setSide(Integer.parseInt(cmd.args[1]));
        		} else {
        			players.put(nick, new Player(nick, Integer.parseInt(cmd.args[1])));
        		}
    		}
    		break;
    		
    		case Command.START_GAME:
    		{
    			frame.getGame().start();
    			frame.disableChat();
    		}
    		break;
    		
    		case Command.STOP_GAME:
    		{
    			frame.getGame().stop();
    			frame.enableChat();
    		}
    		break;
    		
    		case Command.PLAYER_DISCONNECTED:
    		{
    			frame.getGame().stop();
    			this.players.remove(cmd.args[0]);
    		}
    		break;
    		
    		default:  
    			Logger.error("Unknown command: " + message);
                break;
    	}
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
            if(isConnected()) {
            	sendMessage(Command.disconnect());
            	this.connection.kill();
            }
        } catch (IOException e){
            Logger.warn("Temporary silent fail");
        } finally {
        	this.connection = null;
        }
    }
    
    public boolean isConnected(){
    	return this.connection != null;
    }
    
    public void ready(){
    	player.setReady(true);
    	sendMessage(Command.playerReady());
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }
}