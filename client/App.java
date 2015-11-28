package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class App extends JFrame {
	public TCPClient T;
	JTextField chatfield;
	JTextArea chatarea;
	JScrollPane chatscroll;
	static JButton check=new JButton("Check");
	static JButton fold=new JButton("  Fold  ");
	static JButton raise=new JButton("Raise ");
	static JLabel acounttable;
	public App(TCPClient TA){
		T=TA;

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
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setSize(1000,700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel buttons= new JPanel();
		JPanel west=new JPanel();
		JLabel space[] =new JLabel[4];
		acounttable=new JLabel("10000");
		for(int i=0;i<4;i++) {
			  space[i] = new JLabel("  ");
			  space[i].setSize(66, 20);
		}	
		buttons.setSize(66, 200);
		//buttons.setBackground(Color.green);
		west.setSize(67,600);
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
		buttons.add(space[2]);
		buttons.add(check);
		buttons.add(space[0]);
		buttons.add(raise);
		buttons.add(space[1]);
		buttons.add(fold);
		buttons.add(space[3]);
		buttons.add(acounttable);
		check.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					T.sendCommand("$-command-check-$");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		fold.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					T.sendCommand("$-command-fold-$");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		raise.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Raise raisepanel= new Raise(T);
				
			}

		});

		JPanel game = new Game();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.fill=GridBagConstraints.BOTH;
		c.weightx=0.8;   //very powerful magic
		c.weighty=0.85;
		add(game,c);
		c.gridx=0;
		c.gridy=1;
		c.weighty=0.1;
		add(chatscroll,c);
		c.gridx=0;
		c.gridy=2;
		c.weighty=0.0015;
		add(chatfield,c);
		c.gridx=1;
		c.gridy=0;
		c.weightx=0.2/3;
		add(west,c);
		c.gridx=2;
		c.gridy=0;
		c.weightx=0.2/3;
		add(buttons,c);

		this.addWindowListener(new WindowAppListener (T));
	}

}
