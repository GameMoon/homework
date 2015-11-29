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
        value = calculate();
    }

    public int getValue() {
        return value;
    }

    private int calculate() {
        int value = 0;
        if (isRoyalFlush(cards)) return 22;
        if (isStraightFlush(cards)) return 21;
        if (isPoker(cards)) return 20;
        if (isFullHouse(cards)) return 19;
        if (isFlush(cards)) return 18;
        if (isStraight(cards)) return 17;
        if (isDrill(cards)) return 16;
        if (isTwoPair(cards)) return 15;
        if (isPair(cards)) return 14;
        if (player.getCard(0).getValue() > player.getCard(1).getValue()) return player.getCard(0).getValue();
        else return player.getCard(1).getValue();
    }

    private boolean isPair(ArrayList<Card> cards) {
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                if (card1 != card2 && card1.getValue() == card2.getValue()) return true;
            }
        }
        return false;
    }

    private boolean isTwoPair(ArrayList<Card> cards) {
        int numberOfPair = 0;
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                if (card1 != card2 && card1.getValue() == card2.getValue()) numberOfPair++;
                if (numberOfPair == 2) return true;
            }
        }
        return false;
    }

    private boolean isDrill(ArrayList<Card> cards) {
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                for (Card card3 : cards) {
                    if (card1 != card2 && card1 != card3 && card2 != card3 &&
                            card1.getValue() == card2.getValue() && card1.getValue() == card3.getValue())
                        return true;
                }
            }
        }
        return false;
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
        for (Card card1 : cards) {
            for (Card card2 : cards) {
                if (card1 != card2 && card1.getValue() == card2.getValue()) {
                    cards.remove(card1);
                    cards.remove(card2);
                    if (isDrill(cards)) return true;
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
