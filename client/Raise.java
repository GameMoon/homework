package client;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class Raise extends JFrame {
TCPClient T;
JTextField money;
	public Raise(TCPClient t) {
		T=t;
		money=new JTextField();
		money.setColumns(4);
		App.check.setEnabled(false);
		App.raise.setEnabled(false);
		App.fold.setEnabled(false);
		JButton raisebutton= new JButton("Raise");
		raisebutton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int nummer=0;
				char isnummer[]=money.getText().toCharArray();
				for(int i=0;i<isnummer.length;i++){
					
					if((isnummer[i]<='9') && (isnummer[i]>='0')){
						nummer++;
					}
				}
				if(!money.getText().equals("") && nummer==money.getText().length()){
					System.out.println(nummer+"zahl");
					try {
						T.sendCommand("$-command-raise-"+"money.getText()"+"-$");
					} catch (IOException e) {
						e.printStackTrace();
					}
					 
					
					App.check.setEnabled(true);
					App.raise.setEnabled(true);
					App.fold.setEnabled(true);
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(null,"INCORRECT RAISE", "Ooopss...",  JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
		JLabel acount= new JLabel("Your acount: "+App.acounttable.getText());
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

}
