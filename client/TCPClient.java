package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by ugrin on 2015. 10. 27..
 */
public class TCPClient extends Thread{
    private Socket clientsocket;
    private boolean running;
    public TCPClient(int port){
        running = true;
        try {
            clientsocket = new Socket("localhost",port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Stop(){
        running = false;
    }
    public void run(){
        System.out.println("Client is running");
        try {
            DataOutputStream dataout = new DataOutputStream(clientsocket.getOutputStream());
            BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
            String text = null;
            while(running){
                text = bfr.readLine();
                System.out.println("Sent to Server:"+ text);
                dataout.writeBytes(text+ '\n');
            }
            clientsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
