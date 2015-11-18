package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ugrin on 2015. 10. 28..
 */
public class CommandListener extends Thread {
    private GameManager gameManager;
    private boolean running;

    public CommandListener(GameManager gameManager) {
        this.gameManager = gameManager;
        running = true;
        this.start();
    }
    public void Stop(){
        running = false;
    }
    public void run() {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        while (running) {
            String[] cmd = new String[0];
            try {
                cmd = bfr.readLine().split(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (cmd[0].equals("getplayers")) {
                System.out.println("Connected Players:");
                for (Player p : gameManager.getPlayers()) {
                    System.out.println(p.getName());
                }
            } else if (cmd[0].equals("getcommands")) {
                gameManager.getReceievedCommands().forEach((k, c) -> System.out.println(k + ":" + c));
            } else if (cmd[0].equals("numberofcommands")) {
                System.out.println(gameManager.getReceievedCommands().size());
            } else if (cmd[0].equals("exit")) {
                running = false;
            } else if (cmd[0].equals("start")) {
                gameManager.start();
                System.out.println("GameManager started!");
            } else if (cmd[0].equals("stop")) {
                gameManager.Stop();
                System.out.println("GameManager stopped!");
            } else if (cmd[0].equals("sendcommand")) {
                gameManager.tcpServer.sendCommand(gameManager.getPlayers().get(Integer.parseInt(cmd[1])).getSocket(), cmd[2]);
            } else {
                System.out.println("Unknown command");
            }
        }

    }
}
