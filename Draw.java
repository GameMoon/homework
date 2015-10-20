import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ugrin on 2015. 10. 13..
 */
public class Draw extends javax.swing.JPanel {
    int card = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getImageofCard(card),0,0,null);
    }
    public void setCard(int i){
        this.card = i;
        repaint();
    }
    public BufferedImage getImageofCard(int i){
        int widthCard = 79;
        int heightCard = 123;

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("poker.png"));
            int startY = heightCard*(i / 13);
            int startX = widthCard*(i%13);
            image = image.getSubimage(startX,startY,widthCard,heightCard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
