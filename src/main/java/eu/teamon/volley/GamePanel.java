package eu.teamon.volley;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
    public GamePanel(){
		setSize(300, 300);
	}
    
  	public void paintComponent(Graphics g){
		g.setColor(Color.red);
		g.drawRect(50, 50, 200, 250);
	}
}
