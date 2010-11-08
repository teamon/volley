package eu.teamon.volley;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;


/**
 *
 * @author teamon
 */
public class ClientFrame extends JFrame {
    private JButton connectButton;
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel hostLabel;
    private JTextField hostTextField;
    private ChatPanel chatPanel;

    private Client client;

    public ClientFrame() {
        setTitle("Volley Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        
        
        JPanel settingsPane = new JPanel();
        settingsPane.setLayout(new FlowLayout());
                
        // port
        portLabel = new JLabel();
        portLabel.setText("Port:");
        portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPane.add(portLabel);
        
        portTextField = new JTextField();
        portTextField.setText("7777");
        portTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(portTextField);
        
        // host
        hostLabel = new JLabel();
        hostLabel.setText("Host:");
        hostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPane.add(hostLabel);
        
        hostTextField = new JTextField();
        hostTextField.setText("");
        hostTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(hostTextField);
        

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(connectButton);
        
        pane.add(settingsPane);
        
        // Chat
        
        pack();
    }
    
    
    public Client getClient(){
        return this.client;
    }

    protected void connect(){
        try {
            int port = Integer.parseInt(portTextField.getText());
            String host = hostTextField.getText();
            client = new Client(host, port);
            Logger.debug("Client connected");
            portTextField.setEnabled(false);
            hostTextField.setEnabled(false);
            connectButton.setText("Disconnect");
            
            chatPanel = new ChatPanel(this.client.getChat());
            getContentPane().add(chatPanel);
            pack();
            
        } catch (IOException e){
            Logger.error(e.getMessage());
            showError("Wrong port number");
        }
    }

    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
