package eu.teamon.volley.client;

import eu.teamon.volley.utils.Logger;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {
        Logger.setLevel(Logger.DEBUG);
        
        Socket server = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
        try {
            server = new Socket("localhost", 7777);
            out = new PrintWriter(server.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (UnknownHostException e){
            Logger.error(e.getMessage());
            System.exit(1);
        } catch (IOException e){
            Logger.error(e.getMessage());
            System.exit(1);
        }
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        String fromServer;
        
        while((fromServer = in.readLine()) != null){
            Logger.debug("Server: " + fromServer);
            
            String msg = input.readLine();
            if(msg != null){
                Logger.debug("Client: " + msg);
                out.println(msg);
            }
        }
        
        
    }
}