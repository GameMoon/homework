package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JPanel{
	BufferedImage image=null;
	BufferedImage dealerimage=null;
	BufferedImage dealerimagemin=null;
	int di=0;
	ImageIcon icon;
	int flop[]=new int[3];
	int turn=53;
	int river=53;
	static int splitkoord[][]={
		{ 100,200},
		{230,100},
		{400,80},
		{560,100},
		{650,200},
		{560,300},
		{ 400,320},
		{230,300},
	};
	App aa;
	int dealerId=0;
	int actualId=0;
	public Game(App a){
		aa=a;
		flop[0]=53;
		String path = "moveah7.png";
		File file = new File(path);
		String path2= "dealer.png";
		File file2= new File(path2);
		try {
			image = ImageIO.read(file);
			dealerimage=ImageIO.read(file2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon("fireworkmin.gif"," ");
		dealerimagemin=resize(dealerimage,26,26);
		this.setSize(800,600);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this);
		playername(g);
		dealerprint(dealerId, g);
		if(flop[0]!=53){
			flop(flop[0],flop[1],flop[2],g);
		}
		if(turn!=53){
			turn(turn,g);
		}
		if(turn!=53){
			river(river,g);
		}
	}
	public static BufferedImage getImageofCard(int i){
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
	public static   BufferedImage resize(BufferedImage img, int newW, int newH) { 
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}  

	public void flop(int a,int b, int c,Graphics g){
		g.drawImage(getImageofCard(a),260,200,null);
		g.drawImage(getImageofCard(b),320,200,null);
		g.drawImage(getImageofCard(c),380,200,null);
	}
	public void turn(int a,Graphics g){
		g.drawImage(getImageofCard(a),440,200,null);
	}
	public void river(int a,Graphics g){
		g.drawImage(getImageofCard(a),500,200,null);
	}
	public void playername(Graphics g){
		if(aa.name==null){return;}
		g.setColor(Color.YELLOW);
		g.setFont(new Font(null, Font.PLAIN, 15));
		for(int k=0;k<aa.players.keySet().size();k++ ){
			if(k==actualId){g.setColor(Color.CYAN);}
			String player=aa.players.keySet().toArray()[k].toString();
			g.drawString(player, splitkoord[k][0], splitkoord[k][1]-30);
			g.drawString(Integer.toString(aa.players.get(player)[0]), splitkoord[k][0], splitkoord[k][1]-16);
			if((aa.players.get(player)[2])!=53){
				g.drawImage(getImageofCard(aa.players.get(player)[2]),splitkoord[k][0]+10,splitkoord[k][1]-10,null);
			}
			if((aa.players.get(player)[1]!=53)){
				g.drawImage(getImageofCard(aa.players.get(player)[1]),splitkoord[k][0],splitkoord[k][1],null);
			}
			g.setColor(Color.YELLOW);
		}
	}
	public void dealerprint(int a,Graphics g){
		if(splitkoord[a][1]>=200 && 650!=splitkoord[a][0]){
			g.drawImage(dealerimagemin, splitkoord[a][0]+70,splitkoord[a][1] , this);
			if(splitkoord[a][0]==650){
				g.drawImage(dealerimagemin, splitkoord[a][0]-70,splitkoord[a][1] , this);
			}
		}
		else {
			g.drawImage(dealerimagemin, splitkoord[a][0],splitkoord[a][1]+80 , this);

		}
	}
}
