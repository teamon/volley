package eu.teamon.volley;

import java.util.Date;

public class Message {
    private Player author;
    private String content;
    private Date time;
    
    public Message(Player author, String content){
        this.author = author;
        this.content = content;
        this.time = new Date();
    }
    
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
