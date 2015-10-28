package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TCPServer extends Thread {
    private boolean running;
    private ServerSocket socket;
    private ArrayList<Socket> connectedClients;
    private HashMap<Socket, String> incommingCommands;

    public TCPServer(int port) {
        running = true;
        connectedClients = new ArrayList<>();
        incommingCommands = new HashMap<>();
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
        String input = null;
        TCPConnectionListener connectionListener = new TCPConnectionListener(this,socket,connectedClients);
        while (running) { //Receiving Data

        }
    }

    public ArrayList<Socket> getClientsSocket() {
        return connectedClients;
    }

    public void sendCommand(Socket client, String data) {
        try {
            DataOutputStream dataout = new DataOutputStream(client.getOutputStream());
            dataout.writeBytes(data + '\n');
        } catch (IOException e) {
            System.err.println("Failed to send command");
        }
    }
    public HashMap<Socket, String> getCommands() {
        return incommingCommands;
    }

    public void addCommand(Socket socket,String command){
        incommingCommands.put(socket,command);
    }
    public void disconnect(Socket socket,Constants.Connection con){
        connectedClients.remove(socket);
        if(con == Constants.Connection.TIMED_OUT){
        sendCommand(socket, "closed");}
    }

}
