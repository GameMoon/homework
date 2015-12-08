package server;

import java.net.Socket;
import java.text.Normalizer;
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
    public TableManager getTableManager(){ return tableManager;}
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
                        tableManager.getTable(currentPlayer.getTableId()).getGame().players.remove(currentPlayer);
                        tableManager.getTable(currentPlayer.getTableId()).getGame().refreshPlayerData();
                    }
                    catch (NoSuchElementException e){}
                    usedCommands.add(socket);
                } else if (command[1].equals("register")) {
                    if(dataBase.addNewUser(command[2],command[3])){
                        writeText("New User registered "+command[2]+" - "+command[3]);
                        tcpServer.sendCommand(socket,"$-ok-$");
                    }
                    else tcpServer.sendCommand(socket,"$-error-userisalreadyexists-$");
                    usedCommands.add(socket);
                } else if (command[1].equals("chat")) {
                    Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    writeText("[Chat] " + sender.getName() + ":" + command[2]);
                    players.forEach((p) -> {
                        if(sender.getTableId() == p.getTableId())
                            this.tcpServer.sendCommand(p.getSocket(), "$-chat-" + sender.getName() + "-" +Normalizer
                                    .normalize(command[2], Normalizer.Form.NFD)
                                    .replaceAll("[^\\p{ASCII}]", "") + "-$");
                    });
                    usedCommands.add(socket);
                } else if(command[1].equals("ready")){
                    Player sender = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    if(!tableManager.getTable(sender.getTableId()).getGame().isRunning()) {
                        if (sender.isReady()) sender.setReady(false);
                        else sender.setReady(true);
                        tableManager.getTable(sender.getTableId()).getGame().sendMessageAll(sender.getName() + " is ready");
                    }
                    usedCommands.add(socket);
                } else if(command[1].equals("raise")){
                    Player currentPlayer  = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    currentPlayer.setAction(Player.Action.RAISE);
                    currentPlayer.setRaiseing(Integer.parseInt(command[2]));
                    usedCommands.add(socket);
                } else if(command[1].equals("call")){
                    Player currentPlayer  = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    currentPlayer.setAction(Player.Action.CALL);
                    usedCommands.add(socket);
                } else if(command[1].equals("fold")){
                    Player currentPlayer  = players.stream().filter((p) -> p.getSocket() == socket).findFirst().get();
                    currentPlayer.setAction(Player.Action.FOLD);
                    usedCommands.add(socket);
                }

            }
        });
        usedCommands.forEach((key) -> commands.remove(key));
    }

    private void writeText(String text) {
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
        if (!players.contains(newPlayer)){
            players.add(newPlayer);
            tableManager.getTable(tableid).getGame().players.add(newPlayer);

        }
        tcpServer.sendCommand(socket,"$-info-"+newPlayer.getName()+"-"+newPlayer.getMoney()+"-$");
        tcpServer.sendCommand(socket,"$-chat-[Server]-Welcome_"+newPlayer.getName()+"!-$");
        for(Player p : players){
            tableManager.getTable(tableid).getGame().sendPlayerInfo(p.getSocket());
        }

        writeText("Connected: " + name + " password: " + password);
    }
}