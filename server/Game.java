package server;

import java.io.*;
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
    public CopyOnWriteArrayList<Player> players;
    private ArrayList<Card> usedCards;
    private Card[] flop;
    private int pot;
    private int dealerid; //small blind dealerid+1, big blind dealerid+2
    private Random rdm;
    private int tableid;
    private TCPServer tcpServer;
    private int raiseAmmount;
    private int smallBlind = 10;
    private int bigBlind = 20;
    private int bigblindid = 0;
    private int smallblindid = 0;
    private boolean isGameStarted = false;

    public Game(int tableid, TCPServer tcpServer) {
        flop = new Card[5];
        usedCards = new ArrayList<>();
        currentState = -1;
        isRunning = true;
        this.tableid = tableid;
        rdm = new Random();
        pot = 0;
        dealerid = 0;
        this.tcpServer = tcpServer;
        raiseAmmount = 0;
        players = new CopyOnWriteArrayList<>();


    }

    public void setRaiseAmmount(int raiseAmmount) {
        this.raiseAmmount = raiseAmmount;
    }

    public int getRaiseAmmount() {
        return raiseAmmount;
    }

    public boolean isRunning() {
        return isGameStarted;
    }

    public int getCurrentState() {
        return currentState;
    }

    public Card[] getFlop() {
        return flop;
    }

    public void setRunning(boolean state) {
        isRunning = state;
    }

    public void waitingForPlayers() {
        int readyPlayers = 0;
        for (Player currentPlayer : players) {
            if (currentPlayer.isReady() && currentPlayer.getMoney() >= bigBlind) readyPlayers++;
        }
        if (readyPlayers == players.size() && players.size() > 1) currentState++;
        if (players.size() == 0) {
            dealerid = 0;
            pot = 0;
        }
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

    public void sendAllData() {
        String command = "$-playerinfo-";
        for (Player p : players) {
            command += p.getName() + "-" + p.getMoney() + "-" + p.getCard(0).getId() + "-" + p.getCard(1).getId() + "-";
        }
        command += "$";
        for (Player p : players) {
            tcpServer.sendCommand(p.getSocket(), command);
            tcpServer.sendCommand(p.getSocket(), "$-pot-" + pot + "-$");
            tcpServer.sendCommand(p.getSocket(), "$-dealer-" + dealerid + "-$");
        }
    }

    public void sendPlayerInfo(Socket socket) {
        String command = "$-playerinfo-";
        for (Player p : players) {
            command += p.getName() + "-" + p.getMoney() + "-";
            if (p.getSocket() == socket)
                command +=  p.getCard(0).getId() + "-" + p.getCard(1).getId() + "-";
            else {
                if (p.getCard(0).getId() != 53 && p.getCard(0).getId() != 53)
                    command += "52-52-";
                else
                    command += p.getCard(0).getId() + "-" + p.getCard(1).getId() + "-";
            }
        }
        command += "$";
        tcpServer.sendCommand(socket, command);
        tcpServer.sendCommand(socket, "$-pot-" + pot + "-$");
        tcpServer.sendCommand(socket, "$-dealer-" + dealerid + "-$");
    }

    public void refreshPlayerData() {
        for (Player currentPlayer : players) {
            sendPlayerInfo(currentPlayer.getSocket());
        }
    }

    public void sendMessageAll(String message) {
        for (Player p : players) {
            tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-" + message + "-$");
        }
    }

    public int waitingforPlayerAction(int k, int round, boolean blind) {
        try {
            if (players.get(k).isReady()) {
                tcpServer.sendCommand(players.get(k).getSocket(), "$-waitingforyou-$"); //Waiting for player action

                while (players.get(k).getAction() == Player.Action.NONE) ;
                if(raiseAmmount>0 && players.get(k).getAction() == Player.Action.RAISE) players.get(k).setAction(Player.Action.CALL);
                if (players.get(k).getAction() == Player.Action.RAISE) { //RAISE

                    if(blind && k != bigblindid){
                        if(smallblindid == k){
                            if (players.get(k).getRaiseing() + raiseAmmount+smallBlind <= players.get(k).getMoney()) {
                                raiseAmmount += players.get(k).getRaiseing();
                                int money = raiseAmmount - players.get(k).getRaiseAmmount()+smallBlind;
                                players.get(k).setMoney(players.get(k).getMoney() - money);
                                pot += money;
                                players.get(k).setRaiseAmmount(raiseAmmount);
                            }
                        }
                        else{
                            if (players.get(k).getRaiseing() + raiseAmmount+bigBlind <= players.get(k).getMoney()) {
                                raiseAmmount += players.get(k).getRaiseing();
                                int money = raiseAmmount - players.get(k).getRaiseAmmount()+bigBlind;
                                players.get(k).setMoney(players.get(k).getMoney() - money);
                                pot += money;
                                players.get(k).setRaiseAmmount(raiseAmmount);
                            }
                        }
                    }
                    else {
                            raiseAmmount += players.get(k).getRaiseing();
                            int money = raiseAmmount - players.get(k).getRaiseAmmount();
                            players.get(k).setMoney(players.get(k).getMoney() - money);
                            pot += money;
                            players.get(k).setRaiseAmmount(raiseAmmount);
                    }
                    sendMessageAll(players.get(k).getName() + " raised with "+ players.get(k).getRaiseing());
                    players.get(k).setRaiseing(0);
                    players.get(k).setAction(Player.Action.NONE);
                } else if (players.get(k).getAction() == Player.Action.FOLD) { //FOLD
                    players.get(k).getCard(0).setId(53);
                    players.get(k).getCard(1).setId(53);
                    players.get(k).setReady(false);
                    players.get(k).setAction(Player.Action.NONE);
                    sendMessageAll(players.get(k).getName() + " folded");
                } else if (players.get(k).getAction() == Player.Action.CALL) { //CALL
                    if(blind){
                        if(k == bigblindid){
                            if(raiseAmmount > 0){
                                players.get(k).setMoney(players.get(k).getMoney()-raiseAmmount);
                                pot+=raiseAmmount;
                                players.get(k).setRaiseAmmount(raiseAmmount);
                            }
                        }
                        else if(k == smallblindid){
                            if(raiseAmmount > 0){
                                players.get(k).setMoney(players.get(k).getMoney()-raiseAmmount-smallBlind);
                                pot+=raiseAmmount+smallBlind;
                                players.get(k).setRaiseAmmount(raiseAmmount);
                            }
                            else{
                                players.get(k).setMoney(players.get(k).getMoney()-smallBlind);
                                pot+=smallBlind;
                            }
                        }
                        else{
                            if(raiseAmmount > 0){
                                players.get(k).setMoney(players.get(k).getMoney()-raiseAmmount-bigBlind);
                                pot+=raiseAmmount+bigBlind;
                                players.get(k).setRaiseAmmount(raiseAmmount);
                            }
                            else{
                                players.get(k).setMoney(players.get(k).getMoney()-bigBlind);
                                pot+=bigBlind;
                            }
                        }
                    }
                    else{
                        if(raiseAmmount > 0){
                            players.get(k).setMoney(players.get(k).getMoney()-raiseAmmount);
                            pot+=raiseAmmount;
                            players.get(k).setRaiseAmmount(raiseAmmount);
                        }
                    }
                    if(raiseAmmount >0) sendMessageAll(players.get(k).getName() + " called");
                    else sendMessageAll(players.get(k).getName() + " checked");
                    players.get(k).setAction(Player.Action.NONE);
                }
                refreshPlayerData();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("kilepett" + (k - 1));
            return k - 1;
        }
        return k;
    }

    private void sendFlop() {
        for (Player p : players) {
            tcpServer.sendCommand(p.getSocket(), "$-flop-" + flop[0].getId() + "-" + flop[1].getId() + "-" + flop[2].getId() + "-$");
            tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-FLOP-$");
        }
        currentState++;
    }

    private void print(String text) {
        System.out.println("Table[" + tableid + "]: " + text);
    }

    private void waitingforActions(boolean blind) {
        int startindex = dealerid + 1;
        if (startindex == players.size()) startindex = 0;
        if(players.size() == 0){
            dealerid = 0;
            currentState = -1;
            return;
        }
        raiseAmmount = 0;
        for(Player p : players){
            p.setRaiseAmmount(0);
        }
        int round = 0;
        boolean enable = true;
        loop:
        while (enable) {
            for (int k = startindex; k < players.size(); k++) {
                if (players.get(k).isReady()) {
                    if (round == 1 && k == startindex && raiseAmmount == 0) {
                        break loop;
                    }
                    for (Player p : players) {
                        tcpServer.sendCommand(p.getSocket(), "$-active-" + k + "-$");
                    }
                    k = waitingforPlayerAction(k, round, blind);
                }
                if (checkFoldWin()) {
                    break loop;
                }
            }
            for (int k = 0; k < startindex; k++) {
                if (players.get(k).isReady()) {
                    for (Player p : players) {
                        tcpServer.sendCommand(p.getSocket(), "$-active-" + k + "-$");
                    }
                    k = waitingforPlayerAction(k, round, blind);
                }
                if (checkFoldWin()) {
                    break loop;
                }
            }
            int numberofraisedpeople = 0;
            int numberofactiveplayers = 0;
            for (Player p : players) {
                if (p.getRaiseAmmount() == raiseAmmount && p.isReady()) numberofraisedpeople++;
                if (p.isReady()) numberofactiveplayers++;
            }
            if (numberofraisedpeople == numberofactiveplayers) break loop;
            round++;
        }
        currentState++;
    }

    private void sendTurn() {
        for (Player p : players) {
            tcpServer.sendCommand(p.getSocket(), "$-turn-" + flop[3].getId() + "-$");
            tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-TURN-$");
        }
        currentState++;
    }

    private void sendRiver() {
        for (Player p : players) {
            tcpServer.sendCommand(p.getSocket(), "$-river-" + flop[4].getId() + "-$");
            tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-RIVER-$");
        }
        currentState++;
    }

    public void resetGame() {
        dealerid++;
        if (dealerid == players.size()) dealerid = 0;
        smallblindid = dealerid + 1;
        if (smallblindid == players.size()) smallblindid = 0;
        bigblindid = smallblindid + 1;
        if (bigblindid == players.size()) bigblindid = 0;
        for (Player p : players) {
            p.setReady(false);
            tcpServer.sendCommand(p.getSocket(), "$-dealer-" + dealerid + "-$");
        }
        currentState = -1;
    }

    public boolean checkFoldWin() {
        int activePlayers = 0;
        for (Player p : players) {
            if (p.isReady()) activePlayers++;
        }
        if (activePlayers == 1) {
            currentState = 9;
            System.out.println("state:" + currentState);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        super.run();
        while (isRunning) {

            if (currentState == -1) { //Reset parameters
                waitingForPlayers();
            } else if (currentState == 0) {  //Waiting for players
                isGameStarted = true;
                for (Player p : players) {
                    tcpServer.sendCommand(p.getSocket(), "$-river-53-$");
                    tcpServer.sendCommand(p.getSocket(), "$-turn-53-$");
                    tcpServer.sendCommand(p.getSocket(), "$-flop-53-53-53-$");
                    tcpServer.sendCommand(p.getSocket(), "$-dealer-" + dealerid + "-$");
                }
                refreshPlayerData();
                currentState++;
            } else if (currentState == 1) { //Generating cars
                generateCards();
            } else if (currentState == 2) {
                 blind();
            } else if (currentState == 3) { //Blind raise,fold or check
                waitingforActions(true);
            } else if (currentState == 4) { //Flop
                sendFlop();
            } else if (currentState == 5) {
                waitingforActions(false);
            } else if (currentState == 6) { //Turn
                sendTurn();
            } else if (currentState == 7) {
                waitingforActions(false);
            } else if (currentState == 8) { //River
                sendRiver();
            } else if (currentState == 9) {
                waitingforActions(false);
            } else if (currentState == 10) {
                getWinner();
            } else if (currentState == 11) {
                resetGame();
                isGameStarted = false;
            }
        }
    }

    public void getWinner() {
        ArrayList<Player> winners = new ArrayList<>();
        Player winner = null;
        int winnerValue = 0;
        int activePlayers = 0;
        for(Player p: players){
            if(p.isReady()) activePlayers++;
        }
        if(activePlayers == 1) {
            for (Player p : players) {
                winner = p;
            }
            for (Player p : players) {
                tcpServer.sendCommand(p.getSocket(), "$-winner-" + winner.getName() + "-" + pot + "-$");
                tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-The_winner_is_" + winner.getName() + "!-$");
            }
            pot = 0;
            refreshPlayerData();
        }
        else{
            for (Player p : players) {
                if (p.getCard(0).getId() != 53 && p.isReady()) {
                    Combination combo = new Combination(p, flop);
                    int value = combo.getValue();
                    System.out.println(p.getName() + " has a " + Combination.getName(value));
                    if (value > winnerValue) {
                        winnerValue = value;
                        winner = p;
                    }
                }
            }
            for (Player p : players) {
                Combination combo = new Combination(p, flop);
                int value = combo.getValue();
                if (value == winnerValue) {
                    if (winnerValue > 13) {
                        int highcardofWinner = winner.getCard(0).getValue() > winner.getCard(1).getValue() ? winner.getCard(0).getValue() : winner.getCard(1).getValue();
                        int highcardofPlayer = p.getCard(0).getValue() > p.getCard(1).getValue() ? p.getCard(0).getValue() : p.getCard(1).getValue();
                        if (highcardofPlayer > highcardofWinner) {
                            winners.clear();
                            winners.add(p);
                        } else {
                            if(!winners.contains(winner))
                            winners.add(winner);
                        }
                    } else {
                        if(!winners.contains(p)) winners.add(p);
                    }
                }
            }
            //Money split
            for (Player w : winners) {
                w.setMoney(w.getMoney() + pot / winners.size());
            }
            for (Player p : players) {
                for (Player w : winners) {
                    tcpServer.sendCommand(p.getSocket(), "$-winner-" + w.getName() + "-" + pot / winners.size() + "-$");
                    tcpServer.sendCommand(p.getSocket(), "$-chat-[Server]-The_winner_is_" + w.getName() + "_with_" + Combination.getName(winnerValue) + "!-$");
                }
            }
            pot = 0;
            sendAllData();
        }
        //saveGame();

        currentState++;
    }

    public void saveGame() {
        ArchiveGame agame = new ArchiveGame(this);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("games/game" + System.currentTimeMillis() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(agame);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentState++;
        System.out.println("game" + System.currentTimeMillis() + ".ser saved");
    }
    public void loadGame(String name) {
       ArchiveGame e = null;
        try {
            FileInputStream fileIn = new FileInputStream("games/game"+name+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (ArchiveGame) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        System.out.print("Flop: ");
        for(int k = 0;k<5;k++){
            System.out.print("["+flop[k].getId()+"] ");
        }
        System.out.println("");
        for(Player p : e.players){
            System.out.println("Player: "+p.getName()+"Card: ["+p.getCard(0).getId()+"]["+p.getCard(0).getId()+"]");
        }
    }
    public void blind(){
        smallblindid = dealerid + 1;
        if (smallblindid == players.size()) smallblindid = 0;
        bigblindid = smallblindid + 1;
        if (bigblindid == players.size()) bigblindid = 0;
        System.out.println("Bigblind: "+players.get(bigblindid).getName());
        System.out.println("Smallblind: "+players.get(smallblindid).getName());
        players.get(smallblindid).setMoney(players.get(smallblindid).getMoney() - 10);
        players.get(bigblindid).setMoney(players.get(bigblindid).getMoney() - 20);
        pot += 10;
        pot += 20;
        refreshPlayerData();
        currentState++;
    }
}
