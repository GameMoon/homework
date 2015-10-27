package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPConnectionListener extends Thread {
    private ServerSocket serversocket;
    private ArrayList<Socket> clients;
    private boolean running;

    public TCPConnectionListener(ServerSocket serversocket, ArrayList<Socket> clients) {
        this.serversocket = serversocket;
        this.clients = clients;
        running = true;
        this.start();
    }

    public void run() {
        Socket newClient;
        while (running) {
            try {
                newClient = serversocket.accept();
                synchronized (clients) {
                    if (!clients.contains(newClient) && newClient != null) {
                        newClient.setSoTimeout(Constants.maxTimedOut);
                        clients.add(newClient);
                        //System.out.println("Connected " + newClient.getLocalAddress() + ":" + newClient.getPort());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Stop() {
        running = false;
    }
}
