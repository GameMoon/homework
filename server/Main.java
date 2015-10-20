package server;

import java.util.ArrayList;
import java.util.Random;

public class Main {


    public static void main(String[] args){
        Table table = new Table();
        Player john = new Player("John","semmi",400);
        Player bob = new Player("Bob","semmi",310);
        Player valaki = new Player("Valakik","semmi",100);

        table.addPlayer(john);
        table.addPlayer(bob);
        table.addPlayer(valaki);

        System.out.println("Server started V0.1");

        table.start();
        john.setReady(true);
        bob.setReady(true);
        valaki.setReady(true);

        if(table.getState() == 2){
        System.out.println("John: " + john.getCard(0).getId() + " | " + john.getCard(1).getId());
        System.out.println("Bob: " + bob.getCard(0).getId() + " | " + bob.getCard(1).getId());
        System.out.println("Valaki: " + valaki.getCard(0).getId() + " | " + valaki.getCard(1).getId());
        }


    }
}
