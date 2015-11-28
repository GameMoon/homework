package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel{
	BufferedImage image=null;
	
	public Game(){
		String path = "moveah7.png";
		File file = new File(path);
		
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setSize(800,600);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		 g.drawImage(getImageofCard(5),150,100,null);
		 g.drawImage(getImageofCard(11),141,100,null);
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
		        
		        return resize(image,47,74);
		   }
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
}
