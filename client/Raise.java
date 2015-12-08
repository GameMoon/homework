package client;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Raise extends JFrame {
TCPClient T;
JTextField money;
App app;
JButton raisebutton;
JLabel acount;
	public Raise(TCPClient t,App b) {
		app=b;
		T=t;
		money=new JTextField();
		money.setColumns(4);
		app.bnotallowed();
		raisebutton= new JButton("Raise");
		acount= new JLabel("Your acount: "+app.acounttable.getText());
		raisebutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(digit(money.getText())==false){
					money.setText("");
				}
				if(!money.getText().equals("") && Integer.parseInt(money.getText())>0 && digit(money.getText())==true && Integer.parseInt(money.getText())<=Integer.parseInt(app.acounttable.getText())){
					try {
						T.sendCommand("$-raise-"+money.getText()+"-$");
					} catch (IOException e) {
						e.printStackTrace();
					}	
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(null,"INCORRECT RAISE", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
		
		money.getDocument().addDocumentListener(new DocumentListener(){
						@Override
			public void changedUpdate(DocumentEvent arg0) {
				if(!money.getText().equals("") && Integer.parseInt(money.getText())>0 && digit(money.getText())==true && Integer.parseInt(money.getText())<=Integer.parseInt(app.acounttable.getText())){
				acount.setText("Your acount: "+ Integer.toString(Integer.parseInt(app.acounttable.getText())-Integer.parseInt(money.getText())));
				}
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(!money.getText().equals("") && Integer.parseInt(money.getText())>0 && digit(money.getText())==true && Integer.parseInt(money.getText())<=Integer.parseInt(app.acounttable.getText())){
				acount.setText("Your acount: "+ Integer.toString(Integer.parseInt(app.acounttable.getText())-Integer.parseInt(money.getText())));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(!money.getText().equals("") && Integer.parseInt(money.getText())>0 && digit(money.getText())==true && Integer.parseInt(money.getText())<=Integer.parseInt(app.acounttable.getText())){
				acount.setText("Your acount: "+ Integer.toString(Integer.parseInt(app.acounttable.getText())-Integer.parseInt(money.getText())));
				}
			}
			
		});
		
		acount.setFont(new Font(null, Font.PLAIN, 16));
		
		add(acount,BorderLayout.NORTH);
		add(money);
		add(raisebutton,BorderLayout.SOUTH);
		setSize(170,120);
		setTitle("Raise");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
public static boolean digit(String a){
	int nummer=0;
	char isnummer[]=a.toCharArray();
	for(int i=0;i<isnummer.length;i++){
		
		if((isnummer[i]<='9') && (isnummer[i]>='0')){
			nummer++;
		}
		else return false;
	}
	if(nummer==a.length()){
	return true;
	}
	else return false;
}
}
