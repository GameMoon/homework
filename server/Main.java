package server;

import client.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args){
        /*Table table = new Table();
        Player john = new Player("John","semmi",400);
        Player bob = new Player("Bob","semmi",310);
        Player valaki = new Player("Valakik","semmi",100);

        table.addPlayer(john);
        table.addPlayer(bob);
        table.addPlayer(valaki);

        System.out.println("Server started "+Constants.versionNumber);

        table.start();
        john.setReady(true);
        bob.setReady(true);
        valaki.setReady(true);

        if(table.getState() == 2){
        System.out.println("John: " + john.getCard(0).getId() + " | " + john.getCard(1).getId());
        System.out.println("Bob: " + bob.getCard(0).getId() + " | " + bob.getCard(1).getId());
        System.out.println("Valaki: " + valaki.getCard(0).getId() + " | " + valaki.getCard(1).getId());
        }
        */
        String text = null;
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        try {
           text = bfr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

            if(text.equals("server")){
                TCPServer server = new TCPServer(7658);
                server.start();
            }
            else if(text.equals("client")){
                TCPClient client = new TCPClient(7658);
                client.start();
            }



    }
}
