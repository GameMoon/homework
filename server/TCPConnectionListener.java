package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPConnectionListener extends Thread {
    private ServerSocket serversocket;
    private boolean running;
    private ArrayList<Socket> clients;
    private TCPServer tcpServer;

    public TCPConnectionListener(TCPServer tcpServer,ServerSocket serversocket,ArrayList<Socket> clients) {
        this.serversocket = serversocket;
        this.clients = clients;
        this.tcpServer = tcpServer;
        running = true;
        this.start();
    }

    public void run() {
        Socket newClient;
        while (running) {
            try {
                newClient = serversocket.accept();
                newClient.setSoTimeout(Constants.maxTimedOut);
                new TCPClientManager(newClient,tcpServer).start();
                clients.add(newClient);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void Stop() {
        running = false;
    }
}
