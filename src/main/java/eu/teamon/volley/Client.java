package eu.teamon.volley;

import java.io.*;
import java.net.*;

public class Client implements MessageListener {
    private Chat chat;
    private Player player;
    private ConnectionThread connection;
    
    public Client(String host, int port) throws IOException {
        this.chat = new Chat(this);
        this.player = new Player("Dupa");
        this.connection = new ConnectionThread(this, new Socket(host, port));
        this.connection.start();
    }
    
    public void sendMessage(String message){
        Logger.debug("Client#sendMessage(" + message + ")");
        this.connection.sendMessage(message);
    }
    
    public void processMessage(ConnectionThread from, String message){
        Logger.debug("Client#processMessage(" + message + ")");
        // 
        // String[] chunks = message.split(" ", 2);
        // switch(Integer.parseInt(chunks[0])){
        //     case 'c':
        //         if(chunks.length == 3) chat.addMessage(new Message(new Player(chunks[1]), chunks[2]));
        //         break;
        //     default:
        //         Logger.error("Unknown command: " + message);
        //         break;
        // }
    }
    
    public Chat getChat(){
        return this.chat;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }
}