package server;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class Combination {
    private Player player;
    private int value = 0;
    private ArrayList<Card> cards;

    public Combination(Player player, Card[] flop) {
        this.player = player;
        cards = new ArrayList<>();
        cards.add(player.getCard(0));
        cards.add(player.getCard(1));
        for (int k = 0; k < 5; k++) cards.add(flop[k]);
    }

    public int getValue() {
        return calculate();
    }
    public static String getName(int index){
        if(index == 82341) return "Royalflush";
        else if(index == 82340) return "StraightFlush";
        else if(index == 82339) return "Poker";
        else if(index == 82338) return "FullHouse";
        else if(index == 82337) return "Flush";
        else if(index == 82336) return "Straight";
        else if(index>5881 && index <= 82335) return "Drill";
        else if(index <= 5881 && index > 210) return "TwoPair";
        else if(index >=15 && index <= 210) return "Pair";
        else return "HighCard";
     }
    private int calculate() {
        int value = 0;
        int multiplier = 1;
        if (isRoyalFlush(cards)) return 82341;
        else if (isStraightFlush(cards)) return 82340;
        else if (isPoker(cards)) return 82339;
        else if (isFullHouse(cards)) return 82338;
        else if (isFlush(cards)) return 82337;
        else if (isStraight(cards)) return 82336;
        else if (isDrill(cards) > 0){
            return 5881*isDrill(cards);
        }
        else if (isTwoPair(cards) > 0){
            return 210*isTwoPair(cards);
        }
        else if (isPair(cards) > 0){
            return 15*isPair(cards);
        }
        else {
            if(player.getCard(0).getValue() == 1 || player.getCard(1).getValue() == 1) return 14;
            if(player.getCard(0).getValue() > player.getCard(1).getValue())
                return player.getCard(0).getValue();
            else return player.getCard(1).getValue();
        }

    }

    private int isPair(ArrayList<Card> cards) {
        for(Card card1 : cards){
            for(Card card2 :cards){
                if(card1 != card2 &&card1.getValue()==card2.getValue()){
                    if(card1.getValue() == 1) return 14;
                    else return card1.getValue();
                }
            }
        }
        return 0;
    }//pair

    private int isTwoPair(ArrayList<Card> cards) {
        int value = 0;
        for(Card card1 : cards){
            for(Card card2 :cards){
                if(card1 != card2 && card1.getValue()==card2.getValue()){
                    if(card1.getValue() == 1) value += 14;
                    else value += card1.getValue();

                    ArrayList<Card> tempCards = new ArrayList<>();
                    tempCards.addAll(cards);
                    tempCards.remove(card1);
                    tempCards.remove(card2);
                    if(isPair(tempCards) > 0){
                        value += isPair(tempCards);
                        return value;
                    }
                }

            }

        }
        return 0;
    }//twopair

    private int isDrill(ArrayList<Card> cards) {
        for(Card card1 : cards){
            for(Card card2 : cards){
                for(Card card3: cards){
                    if(card1 != card2 && card1 != card3 && card2 != card3 &&
                            card1.getValue() == card2.getValue() && card1.getValue() == card3.getValue()) {
                        if (card1.getValue() == 1) return 14;
                        else return card1.getValue();
                    }
                }
            }
        }
        return 0;
    }

    private boolean isStraight(ArrayList<Card> cards) {
        for (Card c : cards) {
            boolean isOK = true;
            for (int k = 1; k < 5; k++) {
                if (!isContains(c, k)) isOK = false;
            }
            if (isOK) return true;
        }
        return false;
    }

    private boolean isContains(Card c, int index) {
        for (Card k : cards) {
            if (k != c) {
                if (c.getValue() + index == k.getValue()) return true;
            }
        }
        return false;
    }

    private boolean isFlush(ArrayList<Card> cards) {
        for (Card card1 : cards) {
            int type = 1;
            for (Card card2 : cards) {
                if (card1 != card2 && card1.getType() == card2.getType()) type++;
            }
            if (type >= 5) return true;
        }
        return false;
    }

    private boolean isFullHouse(ArrayList<Card> cards) {
        ArrayList<Card> tempcards = new ArrayList<>();
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                if (card1 != card2 && card1.getValue() == card2.getValue()) {
                    tempcards.clear();
                    tempcards.addAll(cards);
                    tempcards.remove(card1);
                    tempcards.remove(card2);
                    if (isDrill(tempcards) > 0) return true;
                }
            }
        }
        return false;
    }

    private boolean isPoker(ArrayList<Card> cards) {
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                for (Card card3 : cards) {
                    for (Card card4 : cards) {
                        if (card1 != card2 && card1 != card3 && card1 != card4 && card2 != card4 && card2 != card3 &&
                                card3 != card4 && card1.getValue() == card2.getValue() &&
                                card1.getValue() == card3.getValue() && card1.getValue() == card4.getValue()
                                )
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isStraightFlush(ArrayList<Card> cards) {
        if (isStraight(cards) && isFlush(cards)) return true;
        else return false;
    }

    private boolean isRoyalFlush(ArrayList<Card> cards) {
        if (isStraightFlush(cards)) {
            if (isContainsValue(1) && isContainsValue(10) &&
                    isContainsValue(11) && isContainsValue(12) && isContainsValue(13)) {
                return true;
            }
        }
        return false;
    }

    private boolean isContainsValue(int value) {
        for (Card c : cards) {
            if (c.getValue() == value) return true;
        }
        return false;
    }
}
