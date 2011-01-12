package eu.teamon.volley.client;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import eu.teamon.volley.common.Ball;
import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.SmartThread;
import eu.teamon.volley.common.Vec;

/**
 * Client game panel - game display and keyboard input handling
 */
public class Game extends JPanel {
	// settings
	private final int SIZE = 430;
	private final float SCALE = SIZE/100.0f;
	private final int WIDTH = SIZE;
	private final int HEIGHT = SIZE;

	// constant scaling
	private final int PLAYER_WIDTH 	= (int)(SCALE * Config.PLAYER_WIDTH);
	private final int PLAYER_HEIGHT = (int)(SCALE * Config.PLAYER_HEIGHT);
	private final int BALL_RADIUS 	= (int)(SCALE * Config.BALL_RADIUS);
	private final int NET_WIDTH 	= (int)(SCALE * Config.NET_WIDTH);
	private final int NET_HEIGHT 	= (int)(SCALE * Config.NET_HEIGHT);
	
	/**
	 * Client reference
	 */
	private Client client;
	
	/**
	 * Ball representation
	 */
	private Ball ball;
	
	/**
	 * Current set
	 */
	private int set = 0;
	
	/**
	 * Players map (nick -> player)
	 */
	private Map<String, Player> players;

	/**
	 * Display thread
	 */
	private SmartThread gameThread;
	
	/**
	 * Create new game instance with client reference
	 */
    public Game(Client client){
    	this.client = client;
    	this.ball = new Ball();
    	this.players = new HashMap<String, Player>();
    	
		setSize(WIDTH, HEIGHT);
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						Game.this.client.sendMessage(Command.movingLeft(true));
						break;
						
					case KeyEvent.VK_RIGHT:
						Game.this.client.sendMessage(Command.movingRight(true));
						break;
						
					case KeyEvent.VK_UP:
						Game.this.client.sendMessage(Command.jumping(true));
						break;
						
					case KeyEvent.VK_SPACE:
						Game.this.client.sendMessage(Command.serve());
						break;
						
					default:
						break;
				}
			}

			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						Game.this.client.sendMessage(Command.movingLeft(false));
						break;
						
					case KeyEvent.VK_RIGHT:
						Game.this.client.sendMessage(Command.movingRight(false));
						break;
						
					case KeyEvent.VK_UP:
						Game.this.client.sendMessage(Command.jumping(false));
						break;
						
					case KeyEvent.VK_SPACE:
						
						break;
						
					default:
						break;
				}
			}

			public void keyTyped(KeyEvent e) {
				switch(e.getKeyChar()){
					case ' ': // space
						Game.this.client.sendMessage(Command.serve());
						break;
					
					default:
						break;
				}
			}
		});
	}
    
    /**
     * Sets ball position
     * @param pos ball position
     */
    public void setBallPosition(Vec pos){
    	this.ball.setPosition(pos);
    }
    
    /**
     * Set current set number
     */
    public void setSet(int set){
    	this.set = set;
    }
    
    /**
     * Returns current set number
     */
    public int getSet(){
    	return this.set;
    }
    
    /**
     * Set player score
     */
    public void setScore(Player player, int score){
		player.setScore(this.set, score);
    }

    /**
     * Returns players map
     */
    public Map<String, Player> getPlayers(){
    	return this.players;
    }
    
    /**
     * Start game
     */
    public void start(){
    	for(Player player : players.values()){
    		player.resetScore();
    	}
    	
       	gameThread = new SmartThread(){
    		public void run(){
    			while(keep) {
    				repaint();
    				try { Thread.sleep(10); } catch (InterruptedException e){ }
    			}
    		}
    	};
    	
    	gameThread.start();
    	Logger.debug("Game started");
    	requestFocus();
    }
    
    /**
     * Stop game
     */
    public void stop(){
    	if(gameThread != null) {
    		gameThread.kill();
    		gameThread = null;
    	}
    	repaint();
    }
        
    /**
     * Returns player color
     * 
     * Red for client player, else red
     */
    protected Color playerColor(Player player){
    	if(player == client.getPlayer()) return Color.red;
    	else return Color.blue;
    }

    /**
     * Draw game
     */
  	public void paintComponent(Graphics gfx){
  		Graphics2D g = (Graphics2D)gfx;
  		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  		
  		g.setColor(Color.WHITE);
  		g.clearRect(0, 0, WIDTH, HEIGHT);
  		
  		if(gameThread != null && gameThread.isAlive()){			
			// players
			for(Player player : players.values()){
				Vec pos = player.getPosition().scale(SCALE);

				g.setPaint(playerColor(player));
				g.fillOval((int)pos.x-(PLAYER_WIDTH/2), (int)pos.y-(PLAYER_HEIGHT+PLAYER_WIDTH/2), PLAYER_WIDTH, PLAYER_WIDTH);
				g.fillRect((int)pos.x-(PLAYER_WIDTH/2), (int)pos.y-PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
				
				// debug
				// g.setPaint(Color.cyan);
				// g.fillOval((int)pos.x-5, (int)pos.y-5, 10, 10);
				// g.fillOval((int)pos.x-5, (int)(pos.y - PLAYER_HEIGHT)-5, 10, 10);
			}
			
			// ball
			g.setPaint(Color.black);
			Vec ballPos = ball.getPosition().scale(SCALE);
			g.fillOval((int)ballPos.x-(BALL_RADIUS), (int)ballPos.y-BALL_RADIUS, 2*BALL_RADIUS, 2*BALL_RADIUS);
			
			// net
			g.setPaint(Color.black);			
			g.fillRect(WIDTH/2 - NET_WIDTH/2, HEIGHT-NET_HEIGHT, NET_WIDTH, NET_HEIGHT);
			g.fillOval(WIDTH/2 - NET_WIDTH/2, HEIGHT-(NET_HEIGHT+NET_WIDTH/2), NET_WIDTH, NET_WIDTH);
			
			// debug
			// g.setPaint(Color.cyan);
			// g.fillOval((int)ballPos.x-5, (int)ballPos.y-5, 10, 10);
  		}
		
	}
}
