package server;

import java.net.Socket;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Player {
    private final String name;
    private final String password;
    private boolean isReady;
    private int money;
    private int tableId;
    private int tableSeat;
    private Socket socket;
    private Card[] cards;

    public Player(String name,String password,int money,Socket socket){
        this.name = name;
        this.password = password;
        this.money = money;
        isReady = false;
        cards = new Card[2];
        this.socket = socket;
    }
    public Socket getSocket(){ return socket;}
    public Card getCard(int index){return cards[index];}
    public void setCard(int index,Card c){
        cards[index] = c;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public void setMoney(int ammount){
        money = ammount;
    }
    public int getMoney(){
        return money;
    }
    public void setTableId(int id){
        this.tableId = id;
    }
    public int getTableId(){
        return tableId;
    }
    public void setTableSeat(int seat){
        this.tableSeat = seat;
    }
    public int getSeat(){
        return tableSeat;
    }
    public boolean isReady(){
        return this.isReady;
    }
    public void setReady(boolean state){
        this.isReady = state;
    }
}
