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
    
    private Server server;

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
        if(server == null){
            startServer();
        } else {
            stopServer();
        }
    }
    
    protected void stopServer(){
        if(server != null){
            server.stop();
            server = null;
            Logger.debug("Server stopped");
            startStopButton.setText("Start");
            portTextField.setEnabled(true);
        }
    }
    
    protected void startServer(){
        try {
            int port = Integer.parseInt(portTextField.getText());
            server = new Server(port);
            Logger.debug("Server started");
            startStopButton.setText("Stop");
            portTextField.setEnabled(false);
        } catch (Exception e){
            Logger.error(e.getMessage());
            showError("Wrong port number");
        }
    }
    
    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
