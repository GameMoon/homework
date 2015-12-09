package server;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TCPServer extends Thread {
    private boolean running;
    private ServerSocket socket;
    private ArrayList<Socket> connectedClients;
    private Map<Socket, String> incommingCommands;
    private Map<Socket,TCPClientManager> tcpClientManagers;
    private GameManager gameManager;

    public GameManager getGameManager(){ return gameManager;}
    public TCPServer(int port,GameManager gameManager) {
        running = true;
        connectedClients = new ArrayList<>();
        tcpClientManagers = new HashMap<>();
        this.gameManager = gameManager;
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
                newClient.setSoTimeout(Constants.maxTimedOut);
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

    public synchronized void sendCommand(Socket client, String data) {
        try {
            if(client != null) {
                DataOutputStream dataout = new DataOutputStream(client.getOutputStream());
                dataout.writeBytes(data + '\n');
                sleep(100);
            }

        } catch (IOException e) {
            connectedClients.remove(client);
            addCommand(client,"$-stop-$");
            gameManager.wakeup();
            client = null;
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
