package eu.teamon.volley;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.text.*;

/**
 *
 * @author teamon
 */
public class ClientFrame extends JFrame {
	private class EmptyNickException extends Exception {}
	// Settings
    private JButton connectButton;
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JLabel nickLabel;
    private JTextField nickTextField;
    
    //Chat
    private JTextField chatMessageInput;
    private JTextArea chatTextArea;

    private Client client;
    private ClientGame game;
    
    public ClientFrame() {
        setTitle("Volley Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 764, 443);

		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        pane.setLayout(null);
		setContentPane(pane);

        JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settingsPanel.setBounds(448, 6, 310, 139);
        settingsPanel.setLayout(null);
		pane.add(settingsPanel);

        // port
        portLabel = new JLabel("Port:");
		portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		portLabel.setBounds(17, 50, 61, 16);
        settingsPanel.add(portLabel);

        portTextField = new JTextField();
		portTextField.setBounds(90, 44, 213, 28);
        portTextField.setText(Integer.toString(Server.DEFAULT_PORT));
        settingsPanel.add(portTextField);

        // host
        hostLabel = new JLabel("Host:");
		hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		hostLabel.setBounds(17, 22, 61, 16);
        settingsPanel.add(hostLabel);

        hostTextField = new JTextField();
		hostTextField.setBounds(90, 16, 213, 28);
        settingsPanel.add(hostTextField);

        // nick
        nickLabel = new JLabel("Nick:");
		nickLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		nickLabel.setBounds(17, 78, 61, 16);
        settingsPanel.add(nickLabel);

        nickTextField = new JTextField();
		nickTextField.setBounds(90, 72, 213, 28);
        settingsPanel.add(nickTextField);


        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
	            if(client.isConnected()) disconnect();
	            else connect();
            }
        });
		connectButton.setBounds(100, 104, 117, 29);
        settingsPanel.add(connectButton);

        
        pane.add(settingsPanel); // TODO: Extract settingsPanel into separate class
		//pane.add(new GamePanel());
        
        // Chat
        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        chatPanel.setBounds(448, 158, 310, 258);
        chatPanel.setLayout(null);

        chatMessageInput = new JTextField();
        chatMessageInput.setBounds(6, 225, 298, 28);
        chatMessageInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = chatMessageInput.getText();
                if(!msg.equals("")) {
                    client.sendMessage(Command.chatMessage(chatMessageInput.getText()));
                    chatMessageInput.setText("");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setLocation(6, 19);
        scrollPane.setSize(298, 203);


        chatPanel.add(chatMessageInput);
        chatPanel.add(scrollPane);
        
        chatTextArea = new JTextArea();
        scrollPane.setViewportView(chatTextArea);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        ((DefaultCaret)chatTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane.add(chatPanel);
		
		client = new Client(this);
		
		game = new ClientGame(client);
		game.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		game.setBounds(6, 6, 430, 410);
		pane.add(game);
		

    }
    
    public void addChatMessage(Player player, String message){
    	chatTextArea.append(String.format("<%s> %s\n", player.getNick(), message));
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
            if(nick.equals("")){
            	throw new EmptyNickException();
            }
            client.connect(host, port, new Player(nick));
            Logger.debug("Client connected");
            
            // Disable settings
            portTextField.setEnabled(false);
            hostTextField.setEnabled(false);
            nickTextField.setEnabled(false);
            connectButton.setText("Disconnect");
            
            // Enable chat
            chatTextArea.setEnabled(true);
            chatMessageInput.setEnabled(true);
        
    		game.start(); // TEMPORARY!!
        } catch (EmptyNickException e){
        	showError("Nick can't be empty");
        } catch (IOException e){
            Logger.error(e.getMessage());
            showError("Something bad " + e.getMessage());
        }
    }
    
    protected void disconnect(){
        client.disconnect();
        Logger.error("DISCONNECT");
        //game.stop();
        
        // enable settings
        portTextField.setEnabled(true);
        hostTextField.setEnabled(true);
        nickTextField.setEnabled(true);
        connectButton.setText("Connect");
        
        // disable chat
        chatTextArea.setEnabled(false);
        chatMessageInput.setEnabled(false);
    }

    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
