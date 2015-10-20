package server;

import java.util.ArrayList;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Table {
    private static int idCounter;
    private int id;
    private ArrayList<Player> players;
    private Game game;

    public Table(){
        id = idCounter; //unique ID generate
        idCounter++;
        players = new ArrayList<Player>();
        game = new Game(id,players);
    }
    public void addPlayer(Player p){
        players.add(p);
    }
    public void removePlayer(Player p){
        players.remove(p);
    }
    public int getNumberofPlayers(){
        return players.size();
    }
    public boolean isActive(){
        return game.isAlive();
    }
    public int getState(){
        return game.getCurrentState();
    }
    public void start(){
        game.start();
    }
    public void stop(){
        game.setRunning(false);
    }

}
