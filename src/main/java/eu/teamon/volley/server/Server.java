package eu.teamon.volley.server;

import eu.teamon.volley.utils.Logger;
import java.util.*;
import java.io.*;
import java.net.*;


public class Server extends Thread {
    class ConnectionThread extends Thread {
        private Socket client = null;
        private PrintWriter out = null;
        private BufferedReader in = null;
        private boolean keep = true;
        
        
        public ConnectionThread(Socket client){
            super("ConnectionThread");
            this.client = client;
        }

        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream());
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
                String inputLine;
                while(keep && (inputLine = in.readLine()) != null){
                    out.println("message form server");
                }
                
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
        
        public void kill(){
            Logger.debug("Kill ConnectionThread");
            keep = false;
            
            try {
                out.close();
                in.close();
                client.close();   
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }
    
    
    public static final int DEFAULT_PORT = 7777;
    
    private int port;
    private ServerSocket socket = null;
    private List<ConnectionThread> connections;
    private boolean keep = true;
    
    public Server() throws IOException {
        this(DEFAULT_PORT);
    }
    
    public Server(int port) throws IOException {
        super("Server");
        this.port = port;
        this.socket = new ServerSocket(port);
        
        this.connections = new ArrayList<ConnectionThread>();
        
        start();
    }
    
    public void run(){
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e){
            Logger.error(e.getMessage());
        }
        
        while(keep){
            try {
                ConnectionThread ct = new ConnectionThread(socket.accept());
                this.connections.add(ct);
                Logger.debug("Client connected. Clients num: " + this.connections.size());
                ct.start();
            } catch (SocketTimeoutException e){
                // do nothing
                Logger.debug("Socket Timeout");
            } catch (IOException e){
                Logger.error(e.getMessage());
            }
        }
        
        // close all connections
        for(ConnectionThread ct : this.connections){
            ct.kill();
        }
        
        try {
            socket.close();
        } catch (IOException e){
            Logger.error(e.getMessage());
        }
    }
    
    public void kill(){
        Logger.debug("Kill Server");
        keep = false;
    }
    
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
}