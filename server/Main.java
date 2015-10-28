package server;

import client.TCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.println("Server started " + Constants.versionNumber);
        boolean running = true;
        GameManager gameManager = new GameManager();


        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        while (running) {
            String[] cmd = bfr.readLine().split(" ");
            if (cmd[0].equals("getplayers")) {
                System.out.println("Connected Players:");
                for (Player p : gameManager.getPlayers()) {
                    System.out.println(p.getName());
                }
            }
            else if(cmd[0].equals("getcommands")){
                gameManager.getReceievedCommands().forEach((k, c) -> System.out.println(k + ":" + c));
            }
            else if(cmd[0].equals("numberofcommands")){
                System.out.println(gameManager.getReceievedCommands().size());
            }
            else if (cmd[0].equals("exit")) {
                running = false;
            }
            else if (cmd[0].equals("start")) {
                gameManager.start();
                System.out.println("GameManager started!");
            }
            else if (cmd[0].equals("stop")) {
                gameManager.Stop();
                System.out.println("GameManager stopped!");
            }
            else if(cmd[0].equals("sendcommand")){
                gameManager.tcpServer.sendCommand(gameManager.getPlayers().get(Integer.parseInt(cmd[1])).getSocket(),cmd[2]);
            }
            else{
                System.out.println("Unknown command");
            }
        }
    }

}

