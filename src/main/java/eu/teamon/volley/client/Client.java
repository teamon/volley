package eu.teamon.volley.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.ConnectionThread;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.MessageListener;
import eu.teamon.volley.common.Vec;

/**
 * Game client application
 *
 */
public class Client implements MessageListener {
	/**
	 * Main frame
	 */
	private Frame frame;
	
	/**
	 * Client game instance
	 */
	private Game game;
	
	/**
	 * Player 
	 */
	private Player player;
	
	/**
	 * Connection with server
	 */
	private ConnectionThread connection;
	
	/**
	 * Sets frame reference
	 */
	public void setFrame(Frame frame){
		this.frame = frame;
	}
	
	/**
	 * Sets game reference
	 */
	public void setGame(Game game){
		this.game = game;
	}
	
	/**
	 * Returns player
	 */
    public Player getPlayer(){
        return this.player;
    }
	
    /**
     * Connect to game server
     * 
     * @param host Server host
     * @param port Server port
     * @param player Player
     * @throws IOException
     */
    public void connect(String host, int port, Player player) throws IOException {
        this.player = player;
        this.connection = new ConnectionThread(this, new Socket(host, port));
        this.connection.start();
		sendMessage(Command.newPlayerRegistered(player));
    }
    
    /**
     * Disconnect from server
     */
    public void disconnect(){
        if(isConnected()) {
           	sendMessage(Command.disconnect());
           	this.connection.kill();
        	this.connection = null;
        }
    }
    
    /**
     * Checks if there is active connection with server
     */
    public boolean isConnected(){
    	return this.connection != null;
    }
    
    /**
     * Sends ready message to server
     */
    public void ready(){
    	sendMessage(Command.playerReady());
    }
    
    /**
     * Notify frame that player is not ready
     */
    public void notReady(){
    	frame.notReady();      
    }
    
    /**
     * Send command to server
     * @param command Command to be sent
     */
    public void sendMessage(Command command){
    	this.connection.sendMessage(command.toString());
    }
    
    /**
     * Process message sent from server
     */
    public void processMessage(ConnectionThread from, String message){
    	Command cmd = Command.parse(message);
    	    	
    	switch(cmd.id){
    		case Command.SERVER_CHAT_MESSAGE:
    		{
    			Player player = game.getPlayers().get(cmd.args[0]);
    			if(player != null){
            		frame.addChatMessage(player, cmd.args[1]);
            	}
    		}
    		break;
    		
    		case Command.PLAYER_POSITION:
    		{
        		Player player = game.getPlayers().get(cmd.args[0]);
        		if(player != null){
        			player.setPosition(new Vec(Float.parseFloat(cmd.args[1]), Float.parseFloat(cmd.args[2])));
        		}
    		}
    		break;
    		
    		case Command.BALL_POSITION:
    		{
    			game.setBallPosition(new Vec(Float.parseFloat(cmd.args[0]), Float.parseFloat(cmd.args[1])));
    		}
    		break;
    		
    		case Command.PLAYER_REGISTERED:
    		{
    			Map<String, Player> players = game.getPlayers();
    			String nick = cmd.args[0];
        		if(!players.containsKey(nick)){
        			Player p;        			
        			if(this.player.getNick().equals(nick)){
        				p = this.player;
        			} else {
        				p = new Player(nick);
        			}
        			p.setIndex(players.size());
        			players.put(nick, p);
        		}
    		}
    		break;
    		
    		case Command.PLAYER_SIDE:
    		{
    			game.getPlayers().get(cmd.args[0]).setSide(Integer.parseInt(cmd.args[1]));
    		}
    		break;
    		
    		case Command.START_GAME:
    		{
    			game.start();
    			frame.disableChat();
    		}
    		break;
 
    		
    		case Command.NEW_SET:
    		{
    			game.setSet(Integer.parseInt(cmd.args[0]));
    			frame.displayScore();
    		}
    		break;
    		
    		case Command.SCORE:
    		{
    			Player player = game.getPlayers().get(cmd.args[0]);
    			if(player != null){
    				game.setScore(player, Integer.parseInt(cmd.args[1]));
    				frame.displayScore();
            	}
    		}
    		break;
    		
    		case Command.STOP_GAME:
    		{
    			game.stop();
    			frame.enableChat();
    			notReady();
    		}
    		break;
    		
    		case Command.PLAYER_DISCONNECTED:
    		{
    			game.stop();
    			game.getPlayers().remove(cmd.args[0]);
    			notReady();
    		}
    		break;
    		
    		default:  
    			Logger.error("Unknown command: " + message);
                break;
    	}
    }
    
    /**
     * Remove connection - disconnect
     */
    public void remove(ConnectionThread connection){
    	disconnect();
    }

	/**
	 * main - run client application
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	Client client = new Client();
                Frame frame = new Frame(client);
                client.setFrame(frame);
            }
        });
	}

}
