package eu.teamon.volley.chat;

import java.util.Data;

public class Message {
    private Player author;
    private String content;
    private Date time;
    
    public Player getAuthor(){
        return this.author;
    }
    
    public String getContent(){
        return this.content;
    }
    
    public Date getTime(){
        return this.time;
    }
}