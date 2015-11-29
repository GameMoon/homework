package server;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager extends Thread {
    private TableManager tableManager;
    private CopyOnWriteArrayList<Player> players;
    private TCPServer tcpServer;
    private DataBase dataBase;
    private boolean running;

    public GameManager() {
        dataBase = new DataBase();
        players = new CopyOnWriteArrayList<>();
        tcpServer = new TCPServer(Constants.serverPort);
        tableManager = new TableManager(Constants.defualtTableNumber,tcpServer);
        running = true;
        start();
        System.out.println("GameManager started");
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
                    System.out.println(command[2]+","+command[3]);
                    boolean isOK = true;
                    for(Player p : players) if(p.getName().equals(command[2])) isOK = false;
                    if(dataBase.checkCredentials(command[2],command[3]) && isOK){  tcpServer.sendCommand(socket,"$-ok-$");}
                    else tcpServer.sendCommand(socket,"$-error-wrongusernameorpassword-$");
                    usedCommands.add(socket);
                } else if (command[1].equals("start")) {
                    newPlayer(command[2],command[3],socket);
                    usedCommands.add(socket);
                } else if (command[1].equals("stop") || command[1].equals("timedout")) {
                    try{ Player currentPlayer  = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                         dataBase.setMoney(currentPlayer.getName(),currentPlayer.getMoney());
                         players.remove(currentPlayer);
                    }
                    catch (NoSuchElementException e){}
                    usedCommands.add(socket);
                } else if (command[1].equals("register")) {
                    if(dataBase.addNewUser(command[2],command[3])){
                        sendMessage("New User registered "+command[2]+" - "+command[3]);
                        tcpServer.sendCommand(socket,"$-ok-$");
                    }
                    else tcpServer.sendCommand(socket,"$-error-userisalreadyexists-$");
                    usedCommands.add(socket);
                } else if (command[1].equals("chat")) {
                    Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    sendMessage("[Chat] " + sender.getName() + ":" + command[2]);
                    players.forEach((p) -> {
                        if(sender.getTableId() == p.getTableId())
                        this.tcpServer.sendCommand(p.getSocket(), "$-chat-" + sender.getName() + "-" + command[2] + "-$");
                    });
                    usedCommands.add(socket);
                } else if(command[1].equals("ready")){
                    Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    if(sender.isReady()) sender.setReady(false);
                    else sender.setReady(true);
                    System.out.println(sender.getName()+" is ready");
                    usedCommands.add(socket);
                }
            }
        });
        usedCommands.forEach((key) -> commands.remove(key));
    }

    private void sendMessage(String text) {
        System.out.println("[Server]" + text);
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public void Stop() {
        running = false;
    }
    public void newPlayer(String name,String password,Socket socket){
        int tableid = 0;
        while(tableManager.getTable(tableid).isFull()) tableid++;
        Player newPlayer = new Player(name, password, dataBase.getMoney(name), socket);
        newPlayer.setTableId(tableid);
        tableManager.getTable(tableid).addPlayer(newPlayer);
        if (!players.contains(newPlayer)) players.add(newPlayer);
        tcpServer.sendCommand(socket,"$-info-"+newPlayer.getName()+"-"+newPlayer.getMoney()+"-$");
        tableManager.getTable(tableid).getGame().sendPlayerInfo(socket);
        sendMessage("Connected: " + name + " password: " + password);
    }
}