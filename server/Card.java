package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ugrin on 2015. 10. 20..
 */
public class Card implements Serializable{
    private int id = 53;

    public Card(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public int getType(){//0 1 2 3
        return id / 13;
    }
    public int getValue(){// 2 3 4 5 6 7 8 9 10 11 12 13 14
        return id -(13*getType())+1;
    }
    public void setId(int id){this.id = id;}
    public static Card getRandomCard(ArrayList<Card> deck){

        Random rdm = new Random();

        int cardid = rdm.nextInt(Constants.numberofCards-1);
        while(isContains(cardid,deck)){
            cardid = rdm.nextInt(Constants.numberofCards-1);
        }
        Card generatedCard = new Card(cardid);
        return generatedCard;
    }
    private static boolean isContains(int cardid,ArrayList<Card> deck){
        for(Card c : deck){
            if(c.getId() == cardid){
                return true;
            }
        }
        return false;
    }
}
