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
    private JButton readyButton;
    private JLabel portLabel;
    private JTextField portTextField;
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JLabel nickLabel;
    private JTextField nickTextField;
    
    private JLabel[] scoreNickLabels = new JLabel[2];
    private JLabel[] scoreScoreLabels = new JLabel[2];
    
    //Chat
    private JTextField chatMessageInput;
    private JTextArea chatTextArea;

    private Client client;
    private ClientGame game;
    
    public ClientFrame() {
        setTitle("Volley Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 764, 463);

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
		connectButton.setBounds(57, 104, 117, 29);
        settingsPanel.add(connectButton);
        
        readyButton = new JButton("Ready");
        readyButton.setEnabled(false);
        readyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	client.ready();
            	readyButton.setEnabled(false);           	
            }
        });
        readyButton.setBounds(186, 104, 117, 29);
        settingsPanel.add(readyButton);

        
        pane.add(settingsPanel); // TODO: Extract settingsPanel into separate class
		//pane.add(new GamePanel());
        
        // Chat
        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        chatPanel.setBounds(448, 231, 310, 205);
        chatPanel.setLayout(null);

        chatMessageInput = new JTextField();
        chatMessageInput.setBounds(6, 172, 298, 28);
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
        scrollPane.setSize(298, 153);


        chatPanel.add(chatMessageInput);
        chatPanel.add(scrollPane);
        
        chatTextArea = new JTextArea();
        scrollPane.setColumnHeaderView(chatTextArea);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        ((DefaultCaret)chatTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane.add(chatPanel);
		
		client = new Client(this);
		
		game = new ClientGame(client);
		game.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		game.setBounds(6, 6, 430, 430);
		pane.add(game);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(new TitledBorder(null, "Score", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scorePanel.setBounds(448, 148, 310, 84);
		pane.add(scorePanel);
		scorePanel.setLayout(null);
		
		scoreNickLabels[0] = new JLabel("");
		scoreNickLabels[0].setHorizontalAlignment(SwingConstants.RIGHT);
		scoreNickLabels[0].setBounds(6, 27, 70, 34);
		scorePanel.add(scoreNickLabels[0]);
		
		JLabel labelColon = new JLabel(":");
		labelColon.setHorizontalAlignment(SwingConstants.CENTER);
		labelColon.setBounds(152, 27, 11, 34);
		scorePanel.add(labelColon);
		
		scoreScoreLabels[0] = new JLabel("");
		scoreScoreLabels[0].setHorizontalAlignment(SwingConstants.RIGHT);
		scoreScoreLabels[0].setBounds(81, 27, 70, 34);
		scorePanel.add(scoreScoreLabels[0]);
		
		scoreNickLabels[1] = new JLabel("");
		scoreNickLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		scoreNickLabels[1].setBounds(234, 27, 70, 34);
		scorePanel.add(scoreNickLabels[1]);
		
		scoreScoreLabels[1] = new JLabel("");
		scoreScoreLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		scoreScoreLabels[1].setBounds(163, 27, 70, 34);
		scorePanel.add(scoreScoreLabels[1]);

    }
    
    public void addChatMessage(Player player, String message){
    	chatTextArea.append(String.format("<%s> %s\n", player.getNick(), message));
    }
    
    public void displayScore(int playerNo, int score){
    	scoreScoreLabels[playerNo].setText(Integer.toString(score));
    }
    
    public void displayNick(int playerNo, String nick){
    	scoreNickLabels[playerNo].setText(nick);
    }
    
    public Client getClient(){
        return this.client;
    }
    
    public ClientGame getGame(){
    	return this.game;
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
            
            // disable settings
            portTextField.setEnabled(false);
            hostTextField.setEnabled(false);
            nickTextField.setEnabled(false);
            connectButton.setText("Disconnect");
            
            // enable chat
            enableChat();
            
            // enable ready button
            readyButton.setEnabled(true);
        
//    		game.start(); // TEMPORARY!!
        } catch (EmptyNickException e){
        	showError("Nick can't be empty");
        } catch (IOException e){
            Logger.error(e.getMessage());
            showError("Something bad " + e.getMessage());
        }
    }
    
    protected void disconnect(){
        client.disconnect();
//        game.stop();
        
        // enable settings
        portTextField.setEnabled(true);
        hostTextField.setEnabled(true);
        nickTextField.setEnabled(true);
        connectButton.setText("Connect");
        
        // disable chat
        disableChat();
        
        // disable ready button
        readyButton.setEnabled(false);
    }
    
    public void disableChat(){
    	chatTextArea.setEnabled(false);
        chatMessageInput.setEnabled(false);
    }
    
    public void enableChat(){
    	chatTextArea.setEnabled(true);
        chatMessageInput.setEnabled(true);
    }

    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
