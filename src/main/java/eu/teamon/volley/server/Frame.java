package eu.teamon.volley.server;

import javax.swing.*;

import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Logger;

import java.awt.event.*;
import java.awt.*;
import java.io.IOException;


/**
 * Main server frame
 */
public class Frame extends JFrame {
    private JButton startStopButton;
    private JLabel portLabel;
    private JTextField portTextField;
    private JTextArea logTextArea;
    
    /**
     * Server instance reference
     */
    private Server server;

    /**
     * Creates new server frame
     */
    public Frame() {
        setTitle("Volley Server");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        
        JPanel settingsPane = new JPanel();
        settingsPane.setLayout(new FlowLayout());
                
        portLabel = new JLabel();
        portLabel.setText("Port:");
        portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPane.add(portLabel);
        
        portTextField = new JTextField();
        portTextField.setText(Integer.toString(Config.DEFAULT_PORT));
        portTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(portTextField);

        startStopButton = new JButton();
        startStopButton.setText("Start");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(server == null){
                    startServer();
                } else {
                    stopServer();
                }
            }
        });
        startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(startStopButton);
        
        pane.add(settingsPane);
        
        logTextArea = new JTextArea(20, 40);
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        logTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(scrollPane);

        pack();
    }

    /**
     * Start button action
     */
    protected void startServer(){
        try {
            int port = Integer.parseInt(portTextField.getText());
            server = new Server(port, this);
            log("Server started");
            startStopButton.setText("Stop");
            portTextField.setEnabled(false);
        } catch (IOException e){
            log(e.getMessage());
            showError(e.getMessage());
        }
    }
    
    /**
     * Stop button action
     */
    protected void stopServer(){
        if(server != null){
            server.kill();
            server = null;
            log("Server stopped");
            startStopButton.setText("Start");
            portTextField.setEnabled(true);
        }
    }

    /**
     * Display error dialog
     */
    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Log message in console
     */
    protected void log(String message){
        logTextArea.append(message + "\n");
    }

}
