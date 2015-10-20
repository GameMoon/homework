package server;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Constants {
    public static final int numberofTables = 10;
    public static final int maxPlayersatTable = 8;
    public static final int numberofCards = 52;

    public enum CardType {CLUBS(0),DIAMONDS(1),HEARTH(2),SPADES(3);
        private int value;
        private CardType(int value){
            this.value = value;
        }
    };

}
