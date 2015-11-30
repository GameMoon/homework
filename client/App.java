package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class App extends JFrame {
	App app=this;
	public TCPClient T;
	int one=53;
	int two=53;
	String name;
	JTextField chatfield;
	JTextArea chatarea;
	JScrollPane chatscroll;
	int money;

	int playersnum;   
	ConcurrentHashMap<String,int[]> players= new ConcurrentHashMap<String,int[]>();

	//static JButton check=new JButton("Check");
	JButton fold=new JButton("  Fold  ");
	JButton raise=new JButton("Raise ");
	JButton ready=new JButton("Ready");
	JButton call=new JButton("Check");
	static JLabel acounttable;
	JLabel main;
	static int mainpot=0;
	Game game;
	public App(TCPClient TA,String a){
		T=TA;
		this.setResizable(false);
		
		setSize(1000,700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Poker-"+a);
		game = new Game(this);
		acounttable=new JLabel(""); 
		main= new JLabel(Integer.toString(mainpot)); 
		bnotallowed();
		chatfield= new JTextField();
		chatfield.setColumns(20);
		chatarea= new JTextArea(5,20);
		chatarea.setEditable(false);
		chatarea.setLineWrap(true);
		chatarea.setWrapStyleWord(false);
		DefaultCaret caret= (DefaultCaret) chatarea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatscroll= new JScrollPane(chatarea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ChatReader chatreader=new ChatReader(this);
		chatreader.start();
		chatfield.addKeyListener(new KeyChatListener(this));


		JPanel buttons= new JPanel();
		JPanel mid=new JPanel();
		JPanel west=new JPanel();
		mid.setLayout(new BoxLayout(mid,BoxLayout.X_AXIS));
		JLabel space[] =new JLabel[5];

		for(int i=0;i<5;i++) {
			space[i] = new JLabel("  ");
			space[i].setSize(200, 20);
		}	
		JLabel acountlabel= new JLabel("Your Acount:");
		JLabel mainpotlabel= new JLabel("Main pot:");
		acountlabel.setForeground(Color.yellow);
		mainpotlabel.setForeground(Color.yellow);
		acountlabel.setSize(66, 20);
		buttons.setSize(66, 200);
		Color neu=new Color(153, 5, 5);
		buttons.setBackground(neu);
		mid.add(Box.createHorizontalGlue());
		mid.setBackground(neu);
		mid.add(buttons);
		mid.add(Box.createHorizontalGlue());
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
		buttons.add(space[3]);
		buttons.add(fold);
		buttons.add(space[0]);
		buttons.add(call);
		buttons.add(space[1]);
		buttons.add(raise);
		buttons.add(space[4]);
		buttons.add(acountlabel);
		buttons.add(acounttable);
		buttons.add(mainpotlabel);
		buttons.add(main);
		call.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					T.sendCommand("$-call-$");
				} catch (IOException e) {
					e.printStackTrace();
				}
				bnotallowed();
			}


		});
		fold.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					T.sendCommand("$-fold-$");
				} catch (IOException e) {
					e.printStackTrace();
				}
				bnotallowed();

			}

		});
		raise.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Raise raisepanel= new Raise(T,app);

			}

		});
		ready.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					T.sendCommand("$-ready-$");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ready.setEnabled(false);
			}

		});

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.8;   //very powerful magic
		c.weighty=0.85;
		add(game,c);
		c.gridx=1;
		c.gridy=1;
		c.weightx=0.2;   //very powerful magic
		c.weighty=0.15;
		add(ready,c);
		c.gridx=0;
		c.gridy=1;
		c.weighty=0.1;
		c.weightx=0.8;
		add(chatscroll,c);
		c.gridx=0;
		c.gridy=2;
		c.weighty=0.0015;
		c.weightx=0.8;
		add(chatfield,c);
		c.gridx=1;
		c.gridy=0;
		c.weightx=0.2;
		add(mid,c);
		
		this.addWindowListener(new WindowAppListener (T));
		raise.setBackground(Color.yellow);
		raise.setForeground(Color.black);
		raise.setOpaque(true);
		fold.setBackground(Color.yellow);
		fold.setForeground(Color.black);
		fold.setOpaque(true);
		call.setBackground(Color.yellow);
		call.setForeground(Color.black);
		call.setOpaque(true);
	}
	public Game getGame(){
		return game;
	}
	public void bnotallowed(){
		this.raise.setEnabled(false);
		this.fold.setEnabled(false);
		this.call.setEnabled(false);
	}
	public void ballowed(){
		this.raise.setEnabled(true);
		this.fold.setEnabled(true);
		this.call.setEnabled(true);
	}
}
