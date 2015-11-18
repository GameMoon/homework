package server;

public class Constants {
    public static final int serverPort = 7658;
    public static final int numberofTables = 10;
    public static final int maxPlayersatTable = 8;
    public static final int numberofCards = 52;
    public static final double versionNumber = 0.2;
    public static final int maxTimedOut = 10000;
    public static final int defualtTableNumber = 4;

    public enum Connection { DISCONNECTED,CONNECTED,TIMED_OUT}
    public enum CardType {CLUBS(0),DIAMONDS(1),HEARTH(2),SPADES(3);
        private int value;
        CardType(int value){
            this.value = value;
        }
    }

}
