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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class App extends JFrame {
	int raisecount=0;
	App app=this;
	public TCPClient T;
	int one=53;
	int two=53;
	String name;
	JTextField chatfield;
	JTextArea chatarea;
	JScrollPane chatscroll;
	JLabel acountlabel;
	JLabel mainpotlabel;
	int money;
	int playersnum;   
	Color neu;
	Color gold;
	LinkedHashMap<String,int[]> players= new LinkedHashMap<String,int[]>();

	JButton fold;
	JButton raise;
	JButton ready;
	JButton call;
	JLabel acounttable;
	JLabel main;
	JPanel buttons;
	int mainpot=0;
	Game game;
	ChatReader chatreader = null;

	public App(TCPClient TA,String a){
		TA.setApp(this);
		T=TA;

		this.setResizable(false);

		setSize(1000,700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("Poker-"+a);
		game = new Game(this);
		acounttable=new JLabel(""); 
		main= new JLabel(Integer.toString(mainpot)); 
		fold=new JButton("  Fold  ");
		raise=new JButton("Raise ");
		ready=new JButton("Ready");
		ready.setFont(new Font(null, Font.PLAIN, 25));
		call=new JButton("Check");

		chatfield= new JTextField();
		chatfield.setColumns(20);
		chatarea= new JTextArea(5,20);
		chatarea.setEditable(false);
		chatarea.setLineWrap(true);
		chatarea.setWrapStyleWord(false);
		DefaultCaret caret= (DefaultCaret) chatarea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatscroll= new JScrollPane(chatarea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatreader=new ChatReader(this);
		chatreader.start();
		chatfield.addKeyListener(new KeyChatListener(this));
		bnotallowed();

		buttons= new JPanel();
		JPanel mid=new JPanel();
		JPanel west=new JPanel();
		JLabel copy=new JLabel("Copyright SZB & UD");
		mid.setLayout(new BoxLayout(mid,BoxLayout.X_AXIS));
		JLabel space[] =new JLabel[5];

		for(int i=0;i<5;i++) {
			space[i] = new JLabel("  ");
			space[i].setSize(200, 20);
		}	
		neu=new Color(153, 5, 5);
		gold=new Color(247,181,25);
		acountlabel= new JLabel("Your Acount:");
		mainpotlabel= new JLabel("Main pot:");
		acountlabel.setForeground(gold);
		mainpotlabel.setForeground(gold);
		main.setForeground(gold);
		acounttable.setForeground(gold);
		acountlabel.setSize(66, 20);
		buttons.setSize(66, 200);
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
				raisecount++;

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
		addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {


			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				JOptionPane opt = new JOptionPane("Are you sure?",JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_CANCEL_OPTION); 
				int value = opt.showConfirmDialog(null, "Are you sure?");
				if (value == JOptionPane.YES_OPTION) {
					System.exit(0);
					
				}
				if (value == JOptionPane.NO_OPTION) {
					opt.setVisible(false);
					return;

				}  
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

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
		c.gridx=1;
		c.gridy=2;
		add(copy,c);

		//this.addWindowListener(new WindowAppListener (T));
		raise.setBackground(gold);
		raise.setForeground(Color.black);
		raise.setOpaque(true);
		fold.setBackground(gold);
		fold.setForeground(Color.black);
		fold.setOpaque(true);
		call.setBackground(gold);
		call.setForeground(Color.black);
		call.setOpaque(true);
		ready.setBackground(gold);
		ready.setForeground(Color.black);
		ready.setOpaque(true);
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
