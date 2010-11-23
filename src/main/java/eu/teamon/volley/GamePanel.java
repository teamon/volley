package eu.teamon.volley;

import java.awt.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
    public GamePanel(){
		setSize(430, 410);
	}
    
  	public void paintComponent(Graphics g){
		g.setColor(Color.red);
		g.drawRect(50, 50, 200, 250);
	}
}
