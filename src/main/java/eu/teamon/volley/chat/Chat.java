package eu.teamon.volley.chat;

import java.util.*;

public class Chat {
    private List<Message> messages;
    
    public Chat(){
        this.messages = new ArrayList<Message>();
    }
    
    public void addMessage(Message msg){
        this.messages.add(msg);
    }
    
    
}