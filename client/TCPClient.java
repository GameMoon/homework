package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class TCPClient extends Thread{
    private Socket socket;
    private boolean running;
    private ArrayList<String> incomingCommands;
    public TCPClient(String ip,int port){
        running = true;
        incomingCommands = new ArrayList<>();
        try {
            socket = new Socket(ip,port);
            this.start();
        } catch (IOException e) {
            System.err.println("Can't open that Socket ("+ip+":"+port+")");
        }
    }
    public void Stop(){
        running = false;
    }
    public void run(){
        try {
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(running){
                if(socket.getInputStream().available()>0){
                    String command = dataIn.readLine();
                    if(command.equals("closed")) Stop();
                    incomingCommands.add(command);
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCommand(String data) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
        dataOut.writeBytes(data+ '\n');
    }

}
