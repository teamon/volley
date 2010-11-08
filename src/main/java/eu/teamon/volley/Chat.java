package eu.teamon.volley;

import java.util.*;

public class Chat {
    private Client client;
    private List<Message> messages;
    
    public ChatPanel panel;
    
    public Chat(Client client){
        this.client = client;
        this.messages = new ArrayList<Message>();
    }
    
    public void addMessage(Message msg){
        this.messages.add(msg);
        this.panel.addMessage(msg);
    }
    
    public void newMessage(String message){
        this.client.sendMessage(formatMessage(message));
    }
        
    protected String formatMessage(String message){
        return "c " + message;
    }
    
}
