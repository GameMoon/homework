package server;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Game extends Thread{
    private int currentState;
    private boolean isRunning;
    private ArrayList<Player> players;
    private ArrayList<Card> usedCards;
    private Card[] flop;
    private Random rdm;
    private int tableid;

    public Game(int tableid,ArrayList<Player> players){
        flop = new Card[5];
        usedCards = new ArrayList<Card>();
        this.players = players;
        currentState = 0;
        isRunning = true;
        this.tableid = tableid;
        rdm = new Random();
    }
    public int getCurrentState(){
        return currentState;
    }
    public void setRunning(boolean state){
        isRunning = state;
    }
    public void waitingforPlayers(){
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
        currentState++;
    }
    private void print(String text){
        System.out.println("Table["+tableid + "]: "+ text);
    }
    @Override
    public void run() {
        super.run();
        while(isRunning){
            if(currentState == 0){  //Waiting for players
                waitingforPlayers();
            }
            else if(currentState == 1){ //Generating cars
                generateCards();
            }
            else if(currentState == 2){ //call or fold

            }
        }
    }
}
