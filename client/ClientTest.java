package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ClientTest {
    private static String text;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String name = bfr.readLine();
        System.out.println(name + " started");
        TCPClient client = new TCPClient(name, "jelszo", "localhost", 7658);
        new Thread() {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));

            public void run() {
                while (true) {
                    try {
                        text = bfr.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        text = null;
        String lasttext = null;
        while (client.isAlive()) {

            // client.sendCommand(text);
            if (text != null && text != lasttext) {
                client.sendCommand(text);
                lasttext = text;
            }

            String incom = client.getCommand();
            if (incom != null) System.out.println(incom);

        }
    }
}
