package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ugrin on 2015. 10. 27..
 */
public class TCPServer extends Thread{
    private boolean running;
    private ServerSocket socket;
    public TCPServer(int port){
        running = true;
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void Stop(){
        running = false;
    }
    @Override
    public void run() {
        System.out.println("Server is running");
        String input = null;
        try {
            Socket consocket = socket.accept();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(consocket.getInputStream()));
            while(running) {
                input = bfr.readLine();
                System.out.println("Client: " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
