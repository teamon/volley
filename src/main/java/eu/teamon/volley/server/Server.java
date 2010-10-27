package eu.teamon.volley.server;

import eu.teamon.volley.utils.Logger;
import java.io.*;
import java.net.*;

public class Server {
    
    // static class ServerThread extends Thread {
    //     private Socket client = null;
    //     
    //     public ServerThread(Socket client){
    //         super("ServerThread");
    //         this.client = client;            
    //     }
    //     
    //     public void run(){
    //         try {
    //             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    //             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    //             
    //             out.println("Helo");
    //             
    //             String inputLine;
    //             while((inputLine = in.readLine()) != null){
    //                 String outputLine = "got " + inputLine;
    //                 out.println(outputLine);
    //             }
    //             
    //             out.close();
    //             in.close();
    //             client.close();
    //         } catch (IOException e){
    //             Logger.error(e.getMessage());
    //             System.exit(1);
    //         }
    //     }
    // }
    
    // public static void main(String[] args) {
    //     Logger.setLevel(Logger.DEBUG);
    //     
    //     ServerSocket serverSocket = null;
    //     
    //     // 
    //     try {
    //         serverSocket = new ServerSocket(7777);
    //     } catch (IOException e){
    //         System.err.println("IOError");
    //         System.exit(1);
    //     }
    //     
    //     Logger.info("Waiting for connections");
    //     while(true){
    //         Logger.debug("Got new connection");
    //         try {
    //             new ServerThread(serverSocket.accept()).start();
    //         } catch (IOException e){
    //             Logger.error(e.getMessage());
    //         }
    //     }
    // }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
}