package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Game extends Thread {
    private int currentState;
    private boolean isRunning;
    private CopyOnWriteArrayList<Player> players;
    private ArrayList<Card> usedCards;
    private Card[] flop;
    private int pot;
    private int dealerid; //small blind dealerid+1, big blind dealerid+2
    private Random rdm;
    private int tableid;
    private TCPServer tcpServer;
    private boolean isRaiseActive;

    public Game(int tableid, CopyOnWriteArrayList<Player> players, TCPServer tcpServer) {
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
        isRaiseActive = false;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setRunning(boolean state) {
        isRunning = state;
    }

    public void waitingForPlayers() {
        int readyPlayers = 0;
        for (Player currentPlayer : players) {
            if (currentPlayer.isReady()) readyPlayers++;
        }
        if (readyPlayers == players.size() && players.size() > 1) currentState++;
    }

    public void generateCards() {
        for (Player currentPlayer : players) {
            Card c = Card.getRandomCard(usedCards);
            currentPlayer.setCard(0, c);
            usedCards.add(c);
        }
        for (Player currentPlayer : players) {
            Card c = Card.getRandomCard(usedCards);
            currentPlayer.setCard(1, c);
            usedCards.add(c);
        }
        for (int k = 0; k < 5; k++) {
            flop[k] = Card.getRandomCard(usedCards);
            usedCards.add(flop[k]);
        }
        print("Cards generated " + currentState);
        refreshPlayerData();
        print("Cards sent");
        currentState++;
    }

    public void sendPlayerInfo(Socket socket) {
        String command = "$-playerinfo-";
        for (Player p : players) {
            if (p.getSocket() == socket)
                command += p.getName() + "-" + p.getMoney() + "-" + p.getCard(0).getId() + "-" + p.getCard(1).getId() + "-";
            else {
                if (p.getCard(0).getId() != 53 && p.getCard(0).getId() != 53)
                    command += p.getName() + "-" + p.getMoney() + "-52-52-";
                else
                    command += p.getName() + "-" + p.getMoney() + "-" + p.getCard(0).getId() + "-" + p.getCard(1).getId() + "-";
            }

        }
        command += "$";
        tcpServer.sendCommand(socket, command);
        System.out.println(command);
    }

    public void refreshPlayerData() {
        for (Player currentPlayer : players) {
            sendPlayerInfo(currentPlayer.getSocket());
        }
    }

    public void waitingforPlayerAction(int k) {
        if (players.get(k).isReady()) {
            tcpServer.sendCommand(players.get(k).getSocket(), "$-waitingforyou-$"); //Waiting for player action
            while (players.get(k).getAction() == Player.Action.NONE) ;
            if (players.get(k).getAction() == Player.Action.RAISE) { //RAISE
                if (players.get(k).getRaiseAmmount() <= players.get(k).getMoney()) {
                    pot += players.get(k).getRaiseAmmount();
                    players.get(k).setMoney(players.get(k).getMoney() - players.get(k).getRaiseAmmount());
                } else tcpServer.sendCommand(players.get(k).getSocket(), "$-error-notenoughmoney-$");
                players.get(k).setAction(Player.Action.NONE);
            } else if (players.get(k).getAction() == Player.Action.FOLD) { //FOLD
                players.get(k).getCard(0).setId(53);
                players.get(k).getCard(1).setId(53);
                players.get(k).setReady(false);
                players.get(k).setAction(Player.Action.NONE);
            } else if (players.get(k).getAction() == Player.Action.CALL) { //CALL
                if (isRaiseActive) {
                    if (players.get(k).getRaiseAmmount() <= players.get(k).getMoney()) {
                        pot += players.get(k).getRaiseAmmount();
                        players.get(k).setMoney(players.get(k).getMoney() - players.get(k).getRaiseAmmount());
                    } else tcpServer.sendCommand(players.get(k).getSocket(), "$-error-notenoughmoney-$");
                    players.get(k).setAction(Player.Action.NONE);
                }
            }
            refreshPlayerData();
        }
    }
    private void sendFlop(){
        for(Player p : players){
            tcpServer.sendCommand(p.getSocket(),"$-flop-"+flop[0]+"-"+flop[1]+"-"+flop[2]+"-$");
            tcpServer.sendCommand(p.getSocket(),"$-chat-[Server]-FLOP-$");
        }
        currentState++;
    }
    private void print(String text) {
        System.out.println("Table[" + tableid + "]: " + text);
    }
    private void waitingforActions(){
        for(int k = dealerid+1;k<players.size();k++) {
            waitingforPlayerAction(k);
        }
        for(int k = 0;k<dealerid+1;k++) {
            waitingforPlayerAction(k);
        }
        int activePlayers = 0;
        for(Player p : players){
            if(p.isReady()) activePlayers++;
        }
        if(activePlayers == 1) currentState = 8;
        currentState++;
    }
    private void sendTurn(){
        for(Player p: players){
            tcpServer.sendCommand(p.getSocket(),"$-turn-"+flop[3]+"-$");
            tcpServer.sendCommand(p.getSocket(),"$-chat-[Server]-TURN-$");
        }
        currentState++;
    }
    private void sendRiver(){
        for(Player p: players){
            tcpServer.sendCommand(p.getSocket(),"$-river-"+flop[4]+"-$");
            tcpServer.sendCommand(p.getSocket(),"$-chat-[Server]-RIVER-$");
        }
        currentState++;
    }
    public void resetGame(){
        for(Player p : players){
            p.setReady(false);
            dealerid++;
            if(dealerid+1 == players.size()) dealerid = 0;
            tcpServer.sendCommand(p.getSocket(),"$-dealer-"+dealerid+"-$");
        }
        currentState = 0;
    }
    @Override
    public void run() {
        super.run();
        while (isRunning) {
            if (currentState == 0) {  //Waiting for players
                waitingForPlayers();
            } else if (currentState == 1) { //Generating cars
                generateCards();
            } else if (currentState == 2) { //Blind raise,fold or check
                waitingforActions();
            } else if(currentState == 3){ //Flop
                sendFlop();
            } else if(currentState == 4){
                waitingforActions();
            } else if(currentState == 5){
                sendTurn();
            } else if(currentState == 6){
                waitingforActions();
            } else if(currentState == 7){
                sendRiver();
            } else if(currentState == 8){
                getWinner();
            } else if(currentState == 9){
                resetGame();
            }
        }
    }
    public void getWinner(){
        Player winner = null;
        int winnerValue = 0;
        for(Player p : players){
            if(p.getCard(0).getId() != 53){
                int value = new Combination(p,flop).getValue();
                if(value > winnerValue){
                    winnerValue = value;
                    winner = p;
                }
            }
        }
        winner.setMoney(winner.getMoney()+pot);
        pot = 0;
        for(Player p : players) {
            tcpServer.sendCommand(p.getSocket(),"$-winner-" + winner.getName() + "-$");
            tcpServer.sendCommand(p.getSocket(),"$-chat-[Server]-The_winner_is_"+winner.getName()+"_with_"+winnerValue+"!-$");
        }

    }
}
