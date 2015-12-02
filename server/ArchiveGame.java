package server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ugrin on 2015. 11. 30..
 */
public class ArchiveGame implements Serializable{
    public ArrayList<Player> players;
    public Card[] flop;
    public ArchiveGame(Game game) {
        players = new ArrayList<>();
        flop = game.getFlop();
    }
}
