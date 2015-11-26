package server;

import java.net.Socket;
import java.util.*;

public class GameManager extends Thread {
    private TableManager tableManager;
    private ArrayList<Player> players;
    private TCPServer tcpServer;
    private DataBase dataBase;
    private boolean running;

    public GameManager() {
        tableManager = new TableManager(Constants.defualtTableNumber);
        dataBase = new DataBase();
        players = new ArrayList<>();
        tcpServer = new TCPServer(Constants.serverPort);
        running = true;
    }
    public TCPServer getTCPServer(){return tcpServer;}
    public DataBase getDataBase(){ return dataBase;}
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
                if (command[1].equals("verify")) {
                    if(dataBase.checkCredentials(command[2],command[3])){  tcpServer.sendCommand(socket,"$-ok-$");}
                    else tcpServer.sendCommand(socket,"$-error-wrongusernameorpassword-$");
                    usedCommands.add(socket);
                } else if (command[1].equals("start")) {
                    Player newPlayer = new Player(command[2], command[3], dataBase.getMoney(command[2]), socket);
                    sendMessage("Connected: " + newPlayer.getName() + " password: " + newPlayer.getPassword());
                    if (!players.contains(newPlayer)) players.add(newPlayer);
                    usedCommands.add(socket);
                } else if (command[1].equals("stop") || command[1].equals("timedout")) {
                    try{ Player currentPlayer  = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                         dataBase.setMoney(currentPlayer.getName(),currentPlayer.getMoney());
                         players.remove(currentPlayer);
                    }
                    catch (NoSuchElementException e){}
                    usedCommands.add(socket);
                } else if (command[1].equals("register")) {
                    if(dataBase.addNewUser(command[2],command[3]))
                        sendMessage("New User registered"+command[2]+" - "+command[3]);
                    else
                        tcpServer.sendCommand(socket,"$-error-userisalreadyexists-$");
                    usedCommands.add(socket);
                } else if (command[1].equals("chat")) {
                    Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    sendMessage("[Chat] " + sender.getName() + ":" + command[2]);
                    players.forEach((p) -> {
                        this.tcpServer.sendCommand(p.getSocket(), "$-chat-" + sender.getName() + "-" + command[2] + "-$");
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