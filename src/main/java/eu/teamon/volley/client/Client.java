package eu.teamon.volley.client;

import eu.teamon.volley.utils.Logger;

import java.io.*;
import java.net.*;

public class Client {
    private String host;
    private int port;
    private Socket server = null;
    
    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.server = new Socket(host, port);
        
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        
        String msg;
        while((msg = in.readLine()) != null){
            processMessage(msg);
        }
    }
    
    protected void processMessage(String msg){
        Logger.debug("ProcessMessage | " + msg);
    }
    
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // public static void main(String[] args) throws IOException {
    //     Logger.setLevel(Logger.DEBUG);
    //     
    //     Socket server = null;
    //     PrintWriter out = null;
    //     BufferedReader in = null;
    //     
    //     try {
    //         server = new Socket("localhost", 7777);
    //         out = new PrintWriter(server.getOutputStream(), true);
    //         in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    //     } catch (UnknownHostException e){
    //         Logger.error(e.getMessage());
    //         System.exit(1);
    //     } catch (IOException e){
    //         Logger.error(e.getMessage());
    //         System.exit(1);
    //     }
    //     
    //     BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    //     
    //     String fromServer;
    //     
    //     while((fromServer = in.readLine()) != null){
    //         Logger.debug("Server: " + fromServer);
    //         
    //         String msg = input.readLine();
    //         if(msg != null){
    //             Logger.debug("Client: " + msg);
    //             out.println(msg);
    //         }
    //     }
    //     
    //
    // }
}