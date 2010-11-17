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
    private JLabel nickLabel;
    private JTextField nickTextField;
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
        portTextField.setPreferredSize(new Dimension(50, 30));
        portTextField.setText("7777");
        portTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(portTextField);
        
        // host
        hostLabel = new JLabel();
        hostLabel.setText("Host:");
        hostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPane.add(hostLabel);
        
        hostTextField = new JTextField();
        hostTextField.setPreferredSize(new Dimension(250, 30));
        hostTextField.setText("");
        hostTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(hostTextField);
        
        // nick
        nickLabel = new JLabel();
        nickLabel.setText("Nick:");
        nickLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPane.add(nickLabel);
        
        nickTextField = new JTextField();
        nickTextField.setPreferredSize(new Dimension(250, 30));
        // nickTextField.setText("");
        nickTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsPane.add(nickTextField);
        

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(client == null){
                    connect();
                } else {
                    disconnect();
                }
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
        Logger.debug("Connecting...");
        try {
            int port = Integer.parseInt(portTextField.getText());
            String host = hostTextField.getText();
            String nick = nickTextField.getText();
            client = new Client(host, port, new Player(nick));
            Logger.debug("Client connected");
            portTextField.setEnabled(false);
            hostTextField.setEnabled(false);
            nickTextField.setEnabled(false);
            connectButton.setText("Disconnect");
        
            chatPanel = new ChatPanel(this.client.getChat());
            getContentPane().add(chatPanel);
            pack();
        
        } catch (IOException e){
            Logger.error(e.getMessage());
            showError("Something bad " + e.getMessage());
        }
    }
    
    protected void disconnect(){
        client.kill();
        client = null;
        portTextField.setEnabled(true);
        hostTextField.setEnabled(true);
        nickTextField.setEnabled(true);
        connectButton.setText("Connect");
        
        if(chatPanel != null){
            getContentPane().remove(chatPanel);
            chatPanel = null;
            pack();
        }

    }

    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
