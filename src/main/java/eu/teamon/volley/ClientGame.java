package eu.teamon.volley;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class ClientGame extends JPanel {
	private final int WIDTH = 430;
	private final int HEIGHT = 410;

	private final int PLAYER_WIDTH = (int)(WIDTH * Player.WIDTH);
	private final int PLAYER_HEIGHT = (int)(WIDTH * Player.HEIGHT);
	private final int BALL_SIZE = (int)(WIDTH * Ball.SIZE);
	
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
    	requestFocus();
    	
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
    }
    
    public void setBallPosition(Vec<Float> pos){
    	this.ball.setPosition(pos);
    }
    
    public void stop(){
    	if(gameThread != null) gameThread.kill();    	
    }
    
    protected int X(float x){
    	return (int)((x+1f)/2 * WIDTH);
    }
    
    protected int Y(float y){
    	return (int)((1f - y) * HEIGHT);
    }
    
    protected Vec<Integer> coords(Vec<Float> vec){
    	return new IntVec(X(vec.x), Y(vec.y));
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
				Vec<Integer> pos = coords(player.getPosition());
				g.setPaint(playerColor(player));
				
				g.fillOval(pos.x-(PLAYER_WIDTH/2), pos.y-(PLAYER_HEIGHT*3/2), PLAYER_WIDTH, PLAYER_WIDTH);
				
				g.fillRect(pos.x-(PLAYER_WIDTH/2), pos.y-PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_HEIGHT);
			}
			
			// ball
			g.setPaint(Color.black);
			Vec<Integer> ballPos = coords(ball.getPosition());
			g.fillOval(ballPos.x-(BALL_SIZE/2), ballPos.y-(BALL_SIZE/2), BALL_SIZE, BALL_SIZE);
  		}
		
	}
}
