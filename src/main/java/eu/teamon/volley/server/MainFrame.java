package eu.teamon.volley.server;

import eu.teamon.volley.utils.Logger;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


/**
 *
 * @author teamon
 */
public class MainFrame extends JFrame {
    private JButton startStopButton;
    private JLabel portLabel;
    private JTextField portTextField;
    
    private boolean started = false;

    public MainFrame() {
        setTitle("Volley Server");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                
        portLabel = new JLabel();
        portLabel.setText("Port:");
        
        portTextField = new JTextField();
        portTextField.setText("7777");

        
        startStopButton = new JButton();
        startStopButton.setText("Start");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startStopServer();
            }
        });
        
        
        FlowLayout layout = new FlowLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        layout.setAlignment(FlowLayout.TRAILING);
        
        panel.add(portLabel);
        panel.add(portTextField);
        panel.add(startStopButton);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        getContentPane().add(panel, BorderLayout.CENTER);

        pack();
    }
    
    public void startStopServer(){
        if(started){
            stopServer();
            startStopButton.setText("Start");
        } else {
            startServer();
            startStopButton.setText("Stop");
        }
    }
    
    protected void stopServer(){
        started = false;
        Logger.debug("Server stopped");
    }
    
    protected void startServer(){
        started = true;
        Logger.debug("Server started");
    }

}
