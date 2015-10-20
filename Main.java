import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Main{
    public static void main(String[] args) throws IOException {
        Drawer dw = new Drawer();
        dw.setVisible(true);
        dw.setCard(5);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        while(dw.isVisible()){
            int a = Integer.parseInt(bfr.readLine());
            dw.setCard(a);
            System.out.println("image refreshed");
        }
    }
}
