package client;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPClient extends Thread {
    private Socket socket;
    private boolean running;
    private CopyOnWriteArrayList incomingCommands;

    public TCPClient(String ip, int port) {
        running = true;
        incomingCommands = new CopyOnWriteArrayList();


        try {
            socket = new Socket(ip, port);
            this.start();
        } catch (IOException e) {
            System.err.println("Can't open that Socket (" + ip + ":" + port + ")");
        }
    }

    public void Stop() {
        running = false;
    }

    public void run() {
        try {
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (running) {
                if (socket.getInputStream().available() > 0) {
                    String command = dataIn.readLine();
                    System.out.println(command);
                    if (command.equals("closed")) Stop();
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
        dataOut.writeBytes(data + '\n');
    }

    public String getCommand() {
    	String command = "fake";
	        if (!incomingCommands.isEmpty()) {
	            command = (String) incomingCommands.get(incomingCommands.size()-1);
	            incomingCommands.remove(incomingCommands.size()-1);
	            if(command==null){command="fake";}
	        }
        return command;
    }

    public CopyOnWriteArrayList getAllCommand() {
        return incomingCommands;
    }

}
