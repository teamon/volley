package eu.teamon.volley.chat;

import eu.teamon.volley.utils.Logger;
import eu.teamon.volley.client.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {
    private JTextField messageInput;
    private MainFrame parent;
    
    public ChatPanel(MainFrame parent){
        super();
        this.parent = parent;

        setLayout(new FlowLayout());

        messageInput = new JTextField();
        messageInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageInput.setPreferredSize(new Dimension(250, 30));
        messageInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(messageInput.getText());
                messageInput.setText("");
            }
        });

        add(messageInput);
    }
    
    public void sendMessage(String message){
        Client client = parent.getClient();
        if(client != null) client.sendChatMessage(message);
    }
}
