package server;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Table {
    private static int idCounter;
    private int id;
    private CopyOnWriteArrayList<Player> players;
    private Game game;

    public Table(TCPServer tcpServer){
        id = idCounter; //unique ID generate
        idCounter++;
        game = new Game(id,tcpServer);
        game.start();
    }

    public boolean isActive(){
        return game.isAlive();
    }
    public int getState(){
        return game.getCurrentState();
    }
    public boolean isFull(){ return (game.players.size()==8);}
    public Game getGame(){ return game;}
    public void start(){
        game.start();
    }
    public void stop(){
        game.setRunning(false);
    }

}
