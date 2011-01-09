package eu.teamon.volley;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class ClientGame extends JPanel {
	private final int SCALE = 430;
	private final int WIDTH = 430;
	private final int HEIGHT = 430;

	private final int PLAYER_WIDTH = (int)(SCALE * Player.WIDTH/2);
	private final int PLAYER_HEIGHT = (int)(SCALE * Player.HEIGHT/2);
	private final int BALL_SIZE = (int)(SCALE * Ball.SIZE/2);
	private final int NET_WIDTH = (int)(SCALE * Game.NET_WIDTH/2);
	private final int NET_HEIGHT = (int)(SCALE * Game.NET_HEIGHT/2);
	
	private Client client;
	private Ball ball;

	private SmartThread gameThread;
	
    public ClientGame(Client client){
    	this.client = client;
    	this.ball = new Ball();
    	
		setSize(WIDTH, HEIGHT);
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						ClientGame.this.client.sendMessage(Command.movingLeft(true));
						break;
						
					case KeyEvent.VK_RIGHT:
						ClientGame.this.client.sendMessage(Command.movingRight(true));
						break;
						
					case KeyEvent.VK_UP:
						ClientGame.this.client.sendMessage(Command.jumping(true));
						break;
						
					case KeyEvent.VK_SPACE:
						ClientGame.this.client.sendMessage(Command.serve());
						break;
						
					default:
						break;
				}
			}

			public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						ClientGame.this.client.sendMessage(Command.movingLeft(false));
						break;
						
					case KeyEvent.VK_RIGHT:
						ClientGame.this.client.sendMessage(Command.movingRight(false));
						break;
						
					case KeyEvent.VK_UP:
						ClientGame.this.client.sendMessage(Command.jumping(false));
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
						ClientGame.this.client.sendMessage(Command.serve());
						break;
					
					default:
						break;
				}
			}
		});
	}
    
    public void start(){
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
    
    public void setBallPosition(Vec pos){
    	this.ball.setPosition(pos);
    }
    
    public void stop(){
    	if(gameThread != null) {
    		gameThread.kill();
    		gameThread = null;
    	}
    	repaint();
    }
    
    protected int X(float x){
    	return (int)((x+1f)/2 * SCALE); // (-1.0 ; 1.0) -> (0.0 ; 1.0)
    }
    
    protected int Y(float y){
    	return (int)((1f - (y/2)) * SCALE); // (2.0 ; 0.0) -> (0.0 ; 1.0)
    }
    
    protected Vec coords(Vec vec){
    	return new Vec(X(vec.x), Y(vec.y));
    }
    
    protected Color playerColor(Player player){
    	switch(player.getSide()){
    		case 1: return Color.red;
    		case -1: return Color.blue;
    		default: return Color.black;
    	}
    }

  	public void paintComponent(Graphics gfx){
  		Graphics2D g = (Graphics2D)gfx;
  		g.setColor(Color.WHITE);
  		g.clearRect(0, 0, WIDTH, HEIGHT);
  		
  		if(gameThread != null && gameThread.isAlive()){			
			// players
			for(Player player : client.getPlayers()){
				Vec pos = coords(player.getPosition());

				
				g.setPaint(playerColor(player));
				g.fillOval((int)pos.x-(PLAYER_WIDTH/2), (int)pos.y-(PLAYER_HEIGHT+PLAYER_WIDTH/2), PLAYER_WIDTH, PLAYER_WIDTH);
				g.fillRect((int)pos.x-(PLAYER_WIDTH/2), (int)pos.y-PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
				
				
				// debug
				g.setPaint(Color.cyan);
				g.fillOval((int)pos.x-5, (int)pos.y-5, 10, 10);
				g.fillOval((int)pos.x-5, (int)(pos.y - PLAYER_HEIGHT)-5, 10, 10);
				// eod
			}
			
			// ball
			g.setPaint(Color.black);
			Vec ballPos = coords(ball.getPosition());
			g.fillOval((int)ballPos.x-(BALL_SIZE/2), (int)ballPos.y-(BALL_SIZE/2), BALL_SIZE, BALL_SIZE);
			
			// net
			g.setPaint(Color.black);			
			g.fillRect(WIDTH/2 - NET_WIDTH/2, HEIGHT-NET_HEIGHT, NET_WIDTH, NET_HEIGHT);
			g.fillOval(WIDTH/2 - NET_WIDTH/2, HEIGHT-(NET_HEIGHT+NET_WIDTH/2), NET_WIDTH, NET_WIDTH);
			
			// debug
			g.setPaint(Color.cyan);
			g.fillOval((int)ballPos.x-5, (int)ballPos.y-5, 10, 10);
			// eod
  		}
		
	}
}
