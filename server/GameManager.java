package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameManager extends Thread{
    public TableManager tableManager;
    public ArrayList<Player> players;
    public TCPServer tcpServer;
    private boolean running;

    public GameManager(){
        tableManager = new TableManager(Constants.defualtTableNumber);
        players = new ArrayList<>();
        tcpServer = new TCPServer(Constants.serverPort);
        running = true;
    }
    public void run(){
        while(running){
            updatePlayers(tcpServer.getCommands());
        }
    }
    public HashMap<Socket,String> getReceievedCommands(){
        return tcpServer.getCommands();
    }
    public void updatePlayers(Map<Socket,String> commands){
       synchronized (commands){

        if(commands.size() > 0){
            ArrayList<Socket> usedCommands = new ArrayList<>();
            for(Socket socket : commands.keySet()){
                String command = commands.get(socket);
                if(command.contains("$-start")){
                    String[] userdata = command.split("-");
                    Player newPlayer = new Player(userdata[2],userdata[3],100,socket);
                    sendMessage(newPlayer.getName()+" is connected");
                    if(!players.contains(newPlayer))  players.add(newPlayer);
                    usedCommands.add(socket);
                }
                else if(command.contains("$-stop")){
                    for(Player p : players){
                        if(p.getSocket() == socket){
                            players.remove(p);
                            sendMessage(p.getName()+" is disconnected");
                            break;
                        }
                    }
                    usedCommands.add(socket);
                }
                else if(command.contains("$-timedout")){
                    for(Player p : players){
                        if(p.getSocket() == socket){
                            players.remove(p);
                            sendMessage(p.getName()+" is timed out");
                            break;
                        }
                    }
                    usedCommands.add(socket);
                }
            }
            for(Socket key: usedCommands){
                commands.remove(key);
            }
        }}
    }
    private void sendMessage(String text){
        System.out.println("[Server] "+text);
    }
    public ArrayList<Player> getPlayers(){
        return players;
    }
    public void Stop(){
        running = false;
    }
}
