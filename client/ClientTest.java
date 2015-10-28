package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by ugrin on 2015. 10. 27..
 */
public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        String name = bfr.readLine();
        System.out.println(name+" started");
        TCPClient client = new TCPClient(name,"jelszo","localhost",7658);
        Random rdm = new Random();
        while(client.isAlive()){
            Thread.sleep(1000);
            client.sendCommand(rdm.nextInt(1000)+"");
            String incom = client.getCommand();
            if(incom != null) System.out.println(incom);

        }
    }
}
