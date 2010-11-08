package eu.teamon.volley.client;

import eu.teamon.volley.*;
import eu.teamon.volley.chat.*;
import eu.teamon.volley.utils.Logger;

import java.io.*;
import java.net.*;

public class Client {
    class ConnectionThread extends Thread {
        private Socket server = null;
        private PrintWriter out = null;
        private BufferedReader in = null;
        private boolean keep = true;
        
        public ConnectionThread(String host, int port) throws UnknownHostException, IOException {
            super("ConnectionThread[Client]");
            this.server = new Socket(host, port);
        }

        public void run() {
            try {
                out = new PrintWriter(server.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            
                String inputLine;
                Logger.debug("Waiting for input...");
                
                while(keep && (inputLine = in.readLine()) != null){
                    Logger.debug("Got: " + inputLine);
                    processMessage(inputLine);
                }
                
                Logger.debug("Waiting no more");
                
                
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
        
        public void sendMessage(String message){
            if(out != null) out.println(message);
        }
        
        public void kill() throws IOException {
            Logger.debug("Kill ConnectionThread");
            keep = false;
            
            out.close();
            in.close();
            server.close();
        }
    }
    
    
    private String host;
    private int port;
    
    private Chat chat;
    private Player player;
    private ConnectionThread connection;
    
    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.chat = new Chat();
        this.player = new Player("Dupa");
        this.connection = new ConnectionThread(host, port);
        this.connection.start();
    }
    
    public void sendMessage(String message){
        Logger.debug("Client#sendMessage(" + message + ")");
        this.connection.sendMessage(message);
    }
    
    public void sendChatMessage(String message){
        sendMessage("c " + "dupa " + message);
    }
    
    protected void processMessage(String message){
        Logger.debug("Client#processMessage(" + message + ")");
        
        String[] chunks = message.split(" ", 2);
        switch(Integer.parseInt(chunks[0])){
            case 'c':
                if(chunks.length == 3) chat.addMessage(new Message(new Player(chunks[1]), chunks[2]));
                break;
            default:
                Logger.error("Unknown command: " + message);
                break;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}