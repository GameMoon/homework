package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClientManager extends Thread {
    private Socket client;
    private TCPServer tcpServer;

    public TCPClientManager(Socket newClient,TCPServer tcpServer) {
        client = newClient;
        this.tcpServer = tcpServer;
    }

    public void run() {
        try {
            while (true) {
                int first = client.getInputStream().read();
                if (first > 0) {
                    BufferedReader dataIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    tcpServer.addCommand(client,(char)(first)+ dataIn.readLine());
                }

            }
        } catch (SocketTimeoutException e) {
            tcpServer.addCommand(client, "$-timedout-$");
            tcpServer.disconnect(client, Constants.Connection.TIMED_OUT);
        } catch (IOException e) {
            tcpServer.addCommand(client, "$-stop-$");
            tcpServer.disconnect(client, Constants.Connection.DISCONNECTED);
        }
    }
}

