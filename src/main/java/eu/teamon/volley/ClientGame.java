package eu.teamon.volley;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class ClientGame extends JPanel {
	private final int WIDTH = 430;
	private final int HEIGHT = 410;
	
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
				Logger.debug("Key typed: " + e.getKeyCode());
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
				Vec<Integer> pos = coords(player.getPos());
				g.setPaint(playerColor(player));
				g.fillRect(pos.x-25, pos.y-50, 50, 50);
			}
			
			// ball
			g.setPaint(Color.black);
			Vec<Integer> ballPos = coords(ball.getPos());
			g.fillOval(ballPos.x-20, ballPos.y-20, 40, 40);
  		}
		
	}
}
