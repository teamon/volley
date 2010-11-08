package eu.teamon.volley;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class ChatPanel extends JPanel {
    private Chat chat;
    private JTextField messageInput;
    private JTextArea textArea;
    
    public ChatPanel(Chat chat){
        super();
        this.chat = chat;
        chat.panel = this;

        setLayout(new FlowLayout());

        messageInput = new JTextField();
        messageInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageInput.setPreferredSize(new Dimension(250, 30));
        messageInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = messageInput.getText();
                if(!msg.equals("")) {
                    ChatPanel.this.chat.newMessage(messageInput.getText());
                    messageInput.setText("");
                }
            }
        });
        
        textArea = new JTextArea();
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        ((DefaultCaret)textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(250, 200));


        add(messageInput);
        add(scrollPane);
    }
    
    public void addMessage(Message message){
        textArea.append(String.format("<%s> %s\n", message.getAuthor().getNick(), message.getContent()));
    }
}
