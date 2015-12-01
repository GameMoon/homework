package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TCPServer extends Thread {
    private boolean running;
    private ServerSocket socket;
    private ArrayList<Socket> connectedClients;
    private Map<Socket, String> incommingCommands;

    public TCPServer(int port) {
        running = true;
        connectedClients = new ArrayList<>();
        incommingCommands = Collections.synchronizedMap(new HashMap<>());
        try {
            this.socket = new ServerSocket(port);
            this.start();
        } catch (IOException e) {
            System.err.println("Failed to start the server");
        }
    }

    public void Stop() {
        running = false;
    }

    @Override
    public void run() {
        Socket newClient;
        while (running) { //Receiving Data
            try {
                newClient = socket.accept();
               // newClient.setSoTimeout(Constants.maxTimedOut); ideiglenes
                new TCPClientManager(newClient,this).start();
                connectedClients.add(newClient);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Socket> getClientsSocket() {
        return connectedClients;
    }

    public void sendCommand(Socket client, String data) {
        try {
            DataOutputStream dataout = new DataOutputStream(client.getOutputStream());
            dataout.writeBytes(data + '\n');
            Thread.sleep(Constants.delay);
        } catch (IOException e) {
            System.err.println("Failed to send command");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Map<Socket, String> getCommands() {
        return incommingCommands;
    }

    public void addCommand(Socket socket,String command){
            incommingCommands.put(socket, command);
    }
    public void disconnect(Socket socket,Constants.Connection con){
        connectedClients.remove(socket);
        if(con == Constants.Connection.TIMED_OUT){
        sendCommand(socket, "closed");}
    }

}
