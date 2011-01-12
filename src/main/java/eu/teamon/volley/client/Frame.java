package eu.teamon.volley.client;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.Font;
import java.io.IOException;
import javax.swing.text.*;

import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.Utils;


/**
 * Client main frame
 */
public class Frame extends JFrame {
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
    private JLabel[] oldScoreLabels = new JLabel[2];
    
    //Chat
    private JTextField chatMessageInput;
    private JTextArea chatTextArea;

    private Client client;
    private Game game;
    
    /**
     * Create frame
     */
    public Frame(Client client) {
    	this.client = client;
    	
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
        portTextField.setText(Integer.toString(Config.DEFAULT_PORT));
        settingsPanel.add(portTextField);

        // host
        hostLabel = new JLabel("Host:");
		hostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		hostLabel.setBounds(17, 22, 61, 16);
        settingsPanel.add(hostLabel);

        hostTextField = new JTextField();
		hostTextField.setBounds(90, 16, 213, 28);
		hostTextField.setText(Config.DEFAULT_HOST);
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
	            if(Frame.this.client.isConnected()) disconnect();
	            else connect();
            }
        });
		connectButton.setBounds(57, 104, 117, 29);
        settingsPanel.add(connectButton);
        
        readyButton = new JButton("Ready");
        readyButton.setEnabled(false);
        readyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ready();    	
            }
        });
        readyButton.setBounds(186, 104, 117, 29);
        settingsPanel.add(readyButton);

        
        pane.add(settingsPanel); // TODO: Extract settingsPanel into separate class
		//pane.add(new GamePanel());
        
        // Chat
        JPanel chatPanel = new JPanel();
        chatPanel.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        chatPanel.setBounds(448, 291, 310, 145);
        chatPanel.setLayout(null);

        chatMessageInput = new JTextField();
        chatMessageInput.setBounds(6, 112, 298, 28);
        chatMessageInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = chatMessageInput.getText();
                if(!msg.equals("")) {
                    Frame.this.client.sendMessage(Command.chatMessage(chatMessageInput.getText()));
                    chatMessageInput.setText("");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setLocation(6, 19);
        scrollPane.setSize(298, 94);


        chatPanel.add(chatMessageInput);
        chatPanel.add(scrollPane);
        
        chatTextArea = new JTextArea();
        scrollPane.setColumnHeaderView(chatTextArea);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        ((DefaultCaret)chatTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		pane.add(chatPanel);
		
		
		game = new Game(client);
		client.setGame(game);
		game.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		game.setBounds(6, 6, 430, 430);
		pane.add(game);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(new TitledBorder(null, "Score", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scorePanel.setBounds(448, 148, 310, 131);
		pane.add(scorePanel);
		scorePanel.setLayout(null);
		
		scoreNickLabels[0] = new JLabel("");
		scoreNickLabels[0].setHorizontalAlignment(SwingConstants.RIGHT);
		scoreNickLabels[0].setBounds(6, 87, 70, 34);
		scorePanel.add(scoreNickLabels[0]);
		
		JLabel labelColon = new JLabel(":");
		labelColon.setHorizontalAlignment(SwingConstants.CENTER);
		labelColon.setBounds(152, 87, 11, 34);
		scorePanel.add(labelColon);
		
		scoreScoreLabels[0] = new JLabel("");
		scoreScoreLabels[0].setHorizontalAlignment(SwingConstants.RIGHT);
		scoreScoreLabels[0].setBounds(81, 87, 70, 34);
		scorePanel.add(scoreScoreLabels[0]);
		
		scoreNickLabels[1] = new JLabel("");
		scoreNickLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		scoreNickLabels[1].setBounds(234, 87, 70, 34);
		scorePanel.add(scoreNickLabels[1]);
		
		scoreScoreLabels[1] = new JLabel("");
		scoreScoreLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		scoreScoreLabels[1].setBounds(163, 87, 70, 34);
		scorePanel.add(scoreScoreLabels[1]);
		
		
		oldScoreLabels[0] = new JLabel("");
		oldScoreLabels[0].setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		oldScoreLabels[0].setHorizontalAlignment(SwingConstants.RIGHT);
		oldScoreLabels[0].setBounds(81, 17, 70, 60);
		scorePanel.add(oldScoreLabels[0]);
		
		oldScoreLabels[1] = new JLabel("");
		oldScoreLabels[1].setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		oldScoreLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		oldScoreLabels[1].setBounds(163, 17, 70, 60);
		scorePanel.add(oldScoreLabels[1]);
		
		scoreScoreLabels[1].setHorizontalAlignment(SwingConstants.LEFT);
		
		setVisible(true);
    }
    
    /**
     * Display chat message
     * @param player Message author
     * @param message Message content
     */
    public void addChatMessage(Player player, String message){
    	chatTextArea.append(String.format("<%s> %s\n", player.getNick(), message));
    }
    
    /**
     * Notify client that player is ready
     */
    protected void ready(){
    	client.ready();
    	readyButton.setEnabled(false);
    }
    
    /**
     * Enable ready button
     */
    protected void notReady(){
    	readyButton.setEnabled(true);
    }
    
    /**
     * Display players score
     */
    public void displayScore(){
    	int set = game.getSet();
    	
		for(Player player : game.getPlayers().values()){
			int i = player.getIndex();
			scoreNickLabels[i].setText(player.getNick());
			scoreScoreLabels[i].setText(Integer.toString(player.getScore()[set]));
			oldScoreLabels[i].setText(Utils.join(player.getScore(game.getSet()), "<html>", "<br/>", "</html>"));
		}
    }
       
    /**
     * Connect button action
     */
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
        } catch (EmptyNickException e){
        	showError("Nick can't be empty");
        } catch (IOException e){
            Logger.error(e.getMessage());
            showError("Something bad " + e.getMessage());
        }
    }
    
    /**
     * Disconnect button action
     */
    protected void disconnect(){
        client.disconnect();
        game.stop();
        
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
    
    /**
     * Disable chat panel
     */
    protected void disableChat(){
    	chatTextArea.setEnabled(false);
        chatMessageInput.setEnabled(false);
    }
    
    /**
     * Enable chat panel
     */
    protected void enableChat(){
    	chatTextArea.setEnabled(true);
        chatMessageInput.setEnabled(true);
    }

    /**
     * Show error dialog
     * @param message
     */
    protected void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
