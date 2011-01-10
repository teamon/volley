package eu.teamon.volley.client;

import java.io.IOException;
import java.net.Socket;

import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.ConnectionThread;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.MessageListener;
import eu.teamon.volley.common.Vec;


public class Client implements MessageListener {
	private Frame frame;
	private Game game;
	private Player player;
	private ConnectionThread connection;
	
	public void setFrame(Frame frame){
		this.frame = frame;
	}
	
	public void setGame(Game game){
		this.game = game;
	}
	
    public Player getPlayer(){
        return this.player;
    }
	
    public void connect(String host, int port, Player player) throws IOException {
        this.player = player;
        this.connection = new ConnectionThread(this, new Socket(host, port));
        this.connection.start();
		sendMessage(Command.newPlayerRegistered(player));
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
    	//player.setReady(true);
    	sendMessage(Command.playerReady());
    }
    
    public void notReady(){
    	//player.setReady(false);
    	frame.notReady();      
    }
    
    
    public void sendMessage(Command command){
    	this.connection.sendMessage(command.toString());
    }
    
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
    			String nick = cmd.args[0];
        		if(!game.getPlayers().containsKey(nick)){
        			Player player = new Player(nick);
        			player.setIndex(game.getPlayers().size());
        			game.getPlayers().put(nick, player);
        		}
        		
        		// TODO: Introduce variable players
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
    
    public void remove(ConnectionThread connection){
        Logger.error("Implement me!");
    }

	/**
	 * @param args
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
