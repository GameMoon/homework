package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

public class TCPServer extends Thread {
    private boolean running;
    private ServerSocket socket;
    private ArrayList<Socket> clients;
    private HashMap<String, String> incommingCommands;

    public TCPServer(int port) {
        running = true;
        clients = new ArrayList<>();
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
        TCPConnectionListener connectionListener = new TCPConnectionListener(socket, clients);
        ArrayList<Socket> timedOutSockets = new ArrayList<>();
        while (running) { //Receiving Data
            synchronized (clients) {
                for (Socket client : clients) {
                    try {
                        int first = client.getInputStream().read();
                        if (first > 0) {
                            BufferedReader dataIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            synchronized (incommingCommands) {
                                incommingCommands.put(client.getInetAddress().getHostAddress() + ":" + client.getPort(), (char)(first) + dataIn.readLine());
                            }
                        }
                    } catch(SocketTimeoutException e){
                        sendCommand(client,"closed");
                        timedOutSockets.add(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                clients.removeAll(timedOutSockets); // Deleting timedout sockets
            }
        }
    }

    public HashMap<String, String> getCommands() {
        return incommingCommands;
    }

    public void sendCommand(Socket client, String data) {
        try {
            DataOutputStream dataout = new DataOutputStream(client.getOutputStream());
            dataout.writeBytes(data + '\n');
        } catch (IOException e) {
           System.err.println("Failed to send command");
        }
    }
}
