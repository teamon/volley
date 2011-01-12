package eu.teamon.volley.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.ConnectionThread;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.MessageListener;
import eu.teamon.volley.common.SmartThread;

/**
 * Game Server application
 *
 */
public class Server extends SmartThread implements MessageListener {
    public static final int CONNECTIONS_LIMIT = 2;

    private ServerSocket socket = null;
    private Frame frame;
    
    /**
     * Connected clients map (connection -> player)
     */
    private Map<ConnectionThread, Player> connections;
    
    /**
     * Game reference
     */
    private Game game;
    
    /**
     * Starts new server at specified port
     */
    public Server(int port, Frame frame) throws IOException {
    	this.frame = frame;
        this.socket = new ServerSocket(port);
        this.connections = new HashMap<ConnectionThread, Player>();     
        this.game = new Game(this);
        start();
    }
    
    /**
     * Process message sent from client
     */
    public void processMessage(ConnectionThread from, String message){
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
    			from.kill(); 
    			
    			this.connections.remove(from);
                log("Client disconnected. Clients left: " + this.connections.size());
    		}
    		break;
    		
    		default:
    			log("Unknown command: " + message);
    		break;
    			
    	}
    }
    
    /**
     * Runs server managing connected client
     */
    public void run(){
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e){
            log(e.getMessage());
        }
        
        while(keep){
            try {
            	if(this.connections.size() < CONNECTIONS_LIMIT){
                    ConnectionThread ct = new ConnectionThread(this, socket.accept());
                    this.connections.put(ct, new Player()); // just an empty player for now
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
                log("Socket Timeout");
            } catch (IOException e){
                log(e.getMessage());
            }
        }
        
        try {
            // close all connections
            for(ConnectionThread ct : connections.keySet()) {
                ct.kill();
            }
            
            socket.close();
        } catch (IOException e){
            log(e.getMessage());
        }
    }
    
    /**
     * Stop game and remove disconnected client and its player
     */
    public void remove(ConnectionThread connection){
    	game.stop();
        this.connections.remove(connection);
    }
    
    /**
     * Send Command to all clients
     */
    public void sendToAll(Command command){
    	String message = command.toString();
    	for (ConnectionThread ct : connections.keySet()) {
            ct.sendMessage(message);
        }
    }
    
    /**
     * Send current set score to all clients
     */
	public void sendScore(){
		for(Player player : getPlayers()){
			sendToAll(Command.score(player, player.getScore()[game.getSet()]));
		}
	}
    
	/**
	 * Returns players collection
	 */
    public Collection<Player> getPlayers(){
    	return connections.values();
    }
    
    /**
     * Tries to start the game first checking if all players are ready
     */
    protected void tryStartGame(){
    	if(ready()){
    		game.start();
			sendToAll(Command.startGame());
    	}
    }
    
    /**
     * Returns true if there is correct number of players and all players are ready
     */
    protected boolean ready(){
    	return (this.connections.size() == CONNECTIONS_LIMIT && allPlayersReady());
    }
    
    /**
     * Returns true if all players are ready
     */
    protected boolean allPlayersReady(){
    	for(Player player : connections.values()){
    		if(!player.isReady()) return false;
    	}
    	return true;
    }
    
    /**
     * Logs message to frame
     */
    public void log(String message){
    	frame.log(message);
    }
    
    /**
     * main - start server application
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }
}
