package eu.teamon.volley;

import java.util.*;

public class Chat {
    private Client client;
    private List<Message> messages;
    
    private ClientFrame frame;
    
    public Chat(Client client, ClientFrame frame){
        this.client = client;
        this.frame = frame;
        this.messages = new ArrayList<Message>();
    }
    
    public void addMessage(Message msg){
        this.messages.add(msg);
        this.frame.addChatMessage(msg);
    }
    
    public void clear(){
    	// TODO: Implement me
    }
    
    public void newMessage(String message){
        this.client.sendMessage(Command.chatMessage(message));
    }
    
}
