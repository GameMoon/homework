package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Game extends Thread{
    private int currentState;
    private boolean isRunning;
    private CopyOnWriteArrayList<Player> players;
    private ArrayList<Card> usedCards;
    private Card[] flop;
    private int pot;
    private int dealerid;
    private Random rdm;
    private int tableid;
    private TCPServer tcpServer;

    public Game(int tableid,CopyOnWriteArrayList<Player> players,TCPServer tcpServer){
        flop = new Card[5];
        usedCards = new ArrayList<Card>();
        this.players = players;
        currentState = 0;
        isRunning = true;
        this.tableid = tableid;
        rdm = new Random();
        pot = 0;
        dealerid = 0;
        this.tcpServer = tcpServer;
    }
    public int getCurrentState(){
        return currentState;
    }
    public void setRunning(boolean state){
        isRunning = state;
    }
    public void waitingForPlayers(){
        int readyPlayers = 0;
        for(Player currentPlayer : players){
            if(currentPlayer.isReady()) readyPlayers++;
        }
        if(readyPlayers == players.size() && players.size() >  1) currentState++;
    }
    public void generateCards(){
        for(Player currentPlayer: players){
            currentPlayer.setCard(0, Card.getRandomCard(usedCards));
        }
        for(Player currentPlayer: players){
            currentPlayer.setCard(1,Card.getRandomCard(usedCards));
        }
        for(int k = 0;k<5;k++){
            flop[k] = Card.getRandomCard(usedCards);
        }
        print("Cards generated " + currentState);
        sendCards();
        print("Cards sent");
        currentState++;
    }
    public void sendPlayerInfo(Socket socket){
        String command = "$-playerinfo-";
        for(Player p : players){
            if(p.getSocket() == socket) command += p.getName()+"-"+p.getMoney()+"-"+p.getCard(0).getId()+"-"+p.getCard(1).getId()+"-";
            else{
                if(p.getCard(0).getId() != 53 && p.getCard(0).getId() != 53) command += p.getName()+"-"+p.getMoney()+"-52-52-";
                else command += p.getName()+"-"+p.getMoney()+"-"+p.getCard(0).getId()+"-"+p.getCard(1).getId()+"-";
            }

        }
        command += "$";
        tcpServer.sendCommand(socket,command);
        System.out.println(command);
    }
    public void sendCards(){
        for(Player currentPlayer: players){
            sendPlayerInfo(currentPlayer.getSocket());
        }
    }
    private void print(String text){
        System.out.println("Table["+tableid + "]: "+ text);
    }
    @Override
    public void run() {
        super.run();
        while(isRunning){
            if(currentState == 0){  //Waiting for players
                waitingForPlayers();
            }
            else if(currentState == 1){ //Generating cars
                generateCards();
            }
            else if(currentState == 2){ //call or fold

            }
          //  if(players.isEmpty()) currentState = 0;
        }
    }
}
