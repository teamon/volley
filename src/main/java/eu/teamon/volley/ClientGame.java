package eu.teamon.volley;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class ClientGame extends JPanel {
	private final int WIDTH = 430;
	private final int HEIGHT = 410;
	
	private Client client;
	private boolean running = false;

	private boolean keyLeftPressed = false;
	private boolean keyRightPressed = false;
	private boolean keyUpPressed = false;
	
	private Thread gameThread;
	
    public ClientGame(Client client){
    	this.client = client;
    	
		setSize(WIDTH, HEIGHT);
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
					case KeyEvent.VK_LEFT:
						keyLeftPressed = true;
						break;
						
					case KeyEvent.VK_RIGHT:
						keyRightPressed = true;
						break;
						
					case KeyEvent.VK_UP:
						keyUpPressed = true;
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
						keyLeftPressed = false;
						break;
						
					case KeyEvent.VK_RIGHT:
						keyRightPressed = false;
						break;
						
					case KeyEvent.VK_UP:
						keyUpPressed = false;
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
    	
    	gameThread = new Thread(){
    		public void run(){
    			while(true) {
    				process();
    				try { Thread.sleep(10); } catch (InterruptedException e){ }
    			}
    		}
    	}; // TODO: Kill me!
    	
    	gameThread.start();
    	Logger.debug("Game started");
    }
    
    private void process(){
    	if(keyLeftPressed){
    		client.sendMessage(Command.moveX(-1));
    	}
    	
    	if(keyRightPressed){
    		client.sendMessage(Command.moveX(1));
    	}

		this.repaint();
    }
    
  	public void paintComponent(Graphics g){
  		g.setColor(Color.WHITE);
  		g.clearRect(0, 0, WIDTH, HEIGHT);
  		
		g.setColor(Color.RED);
		
		for(Player player : client.getPlayers()){
			int x = (int)((player.getX()+1f)/2 * WIDTH);
			int y = (int)((1f - player.getY()) * HEIGHT);
			Logger.debug("x=" + x + ", y=" + y);
			g.fillRect(x-25, y-50, 50, 50);
		}
		
	}
}
