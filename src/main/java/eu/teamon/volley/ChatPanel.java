package eu.teamon.volley;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {
    private JTextField messageInput;
    private Chat chat;
    
    public ChatPanel(Chat chat){
        super();
        this.chat = chat;

        setLayout(new FlowLayout());

        messageInput = new JTextField();
        messageInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageInput.setPreferredSize(new Dimension(250, 30));
        messageInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChatPanel.this.chat.newMessage(messageInput.getText());
                messageInput.setText("");
            }
        });

        add(messageInput);
    }

}
