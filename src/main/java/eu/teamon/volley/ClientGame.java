package eu.teamon.volley;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class ClientGame extends JPanel {
	private final int WIDTH = 430;
	private final int HEIGHT = 410;
	
	private Client client;
	//private boolean running = false;

	private Thread gameThread;
	
    public ClientGame(Client client){
    	this.client = client;
    	
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
    				repaint();
    			}
    		}
    	}; // TODO: Kill me!
    	
    	gameThread.start();
    	Logger.debug("Game started");
    }

  	public void paintComponent(Graphics g){
  		g.setColor(Color.WHITE);
  		g.clearRect(0, 0, WIDTH, HEIGHT);
  		
		g.setColor(Color.RED);
		
		for(Player player : client.getPlayers()){
			int x = (int)((player.getX()+1f)/2 * WIDTH);
			int y = (int)((1f - player.getY()) * HEIGHT);
			g.fillRect(x-25, y-50, 50, 50);
		}
		
	}
}
