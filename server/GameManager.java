package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameManager extends Thread {
    public TableManager tableManager;
    public ArrayList<Player> players;
    public TCPServer tcpServer;
    private boolean running;

    public GameManager() {
        tableManager = new TableManager(Constants.defualtTableNumber);
        players = new ArrayList<>();
        tcpServer = new TCPServer(Constants.serverPort);
        running = true;
    }

    public void run() {
        while (running) {
            updatePlayers(tcpServer.getCommands());
        }
    }

    public Map<Socket, String> getReceievedCommands() {
        return tcpServer.getCommands();
    }

    public void updatePlayers(Map<Socket, String> commands) {
            ArrayList<Socket> usedCommands = new ArrayList<>();
            commands.keySet().forEach((socket) -> {
                String[] command = commands.get(socket).split("-");
                if (command[0].equals("$") && command[command.length - 1].equals("$")) {
                    if (command[1].equals("start")) {
                        Player newPlayer = new Player(command[2], command[3], 100, socket);
                        if (!players.contains(newPlayer)) players.add(newPlayer);
                        usedCommands.add(socket);
                    } else if (command[1].equals("stop")) {
                        players.remove(players.stream().filter((p) -> p.getSocket() == socket).findFirst().get());
                        usedCommands.add(socket);
                    } else if (command[1].equals("timedout")) {
                        players.remove(players.stream().filter((p) -> p.getSocket() == socket).findFirst().get());
                        usedCommands.add(socket);
                    } else if (command[1].equals("chat")) {
                        Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                        sendMessage("[Chat] "+sender.getName()+":"+command[2]);
                        players.forEach((p) -> {
                                this.tcpServer.sendCommand(p.getSocket(),"$-chat-"+sender.getName()+"-"+command[2]+"-$");
                        });
                        usedCommands.add(socket);
                    }
                }
            });
            usedCommands.forEach((key) -> commands.remove(key));
    }

    private void sendMessage(String text) {
        System.out.println("[Server]" + text);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void Stop() {
        running = false;
    }
}