package eu.teamon.volley;

import javax.swing.*;
import javax.swing.border.*;
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
		setBounds(100, 100, 722, 474);

		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        pane.setLayout(null);
		setContentPane(pane);

        JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settingsPanel.setBounds(406, 6, 310, 139);
        settingsPanel.setLayout(null);
		pane.add(settingsPanel);

        // port
        portLabel = new JLabel("Port:");
		portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		portLabel.setBounds(17, 50, 61, 16);
        settingsPanel.add(portLabel);

        portTextField = new JTextField();
		portTextField.setBounds(90, 44, 213, 28);
        portTextField.setText("7777");
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

            }
        });
		connectButton.setBounds(100, 104, 117, 29);
        settingsPanel.add(connectButton);

        
        pane.add(settingsPanel); // TODO: Extract settingsPanel into separate class
		pane.add(new GamePanel());
        
        // Chat
        
        // pack();
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
